package implario.annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SupportedAnnotationTypes("implario.annotations.Groovy")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class GroovyProcessor extends AbstractProcessor {

	private static final Pattern consumerPattern = Pattern.compile("java.util.function.Consumer(<(.+)>)?");

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {

		for (TypeElement annotation : annotations) {
			Set<? extends Element> elements = environment.getElementsAnnotatedWith(annotation);

			for (Element element : elements) {

				if (!(element instanceof ExecutableElement)) continue;
				ExecutableElement el = (ExecutableElement) element;
				for (VariableElement parameter : el.getParameters()) {
					Matcher matcher = consumerPattern.matcher(parameter.asType().toString());
					if (!matcher.find()) continue;
					String genericType = matcher.group(2);
					String delegateType = "java.lang.Object";
					if (genericType != null && !genericType.isEmpty()) {
						String delegateCandidate = genericType.replaceAll("(<.*>|\\? extends |\\? super |\\?)", "");
						if (!delegateCandidate.isEmpty()) delegateType = delegateCandidate;
					}
					processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Delegate type for " + parameter.asType() + " is " + delegateType);

				}
			}
		}
		return false;
	}

}
