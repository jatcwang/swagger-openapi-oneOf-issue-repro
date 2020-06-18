import au.com.dius.pact.model.FileSource;
import au.com.dius.pact.model.Interaction;
import au.com.dius.pact.provider.ConsumerInfo;
import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.pact.PactProviderValidationResults;
import com.atlassian.oai.validator.pact.PactProviderValidator;
import com.atlassian.oai.validator.report.LevelResolver;
import com.atlassian.oai.validator.report.ValidationReport;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        ConsumerInfo consumer = new ConsumerInfo("self");
        consumer.setPactSource(new FileSource<Interaction>(new File("./src/main/resources/pact.json")));
        PactProviderValidator.Builder validatorBuilder = new PactProviderValidator.Builder();
        validatorBuilder.withConsumers(consumer);

        PactProviderValidator validator = validatorBuilder
                .withValidator(
                        OpenApiInteractionValidator
                                .createFor(new File("./src/main/resources/openapi.yaml").getCanonicalPath())
                                .withLevelResolver(LevelResolver.create().withDefaultLevel(ValidationReport.Level.ERROR).build())
                                .build()
                )
                .build();

        PactProviderValidationResults validationResult = validator.validate();
        if (validationResult.hasErrors()) {
            throw new RuntimeException(validationResult.getValidationFailureReport());
        }
    }
}
