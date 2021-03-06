package software.amazon.smithy.openapi.fromsmithy.protocols;

import java.io.InputStream;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.node.Node;
import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;
import software.amazon.smithy.openapi.OpenApiConfig;
import software.amazon.smithy.openapi.fromsmithy.OpenApiConverter;
import software.amazon.smithy.openapi.model.OpenApi;
import software.amazon.smithy.utils.IoUtils;

public class AwsRestJson1ProtocolTest {
    private static final Logger LOGGER = Logger.getLogger(AwsRestJson1ProtocolTest.class.getName());

    @ParameterizedTest
    @ValueSource(strings = {
            "adds-json-document-bodies.json",
            "adds-path-timestamp-format.json",
            "adds-query-timestamp-format.json",
            "adds-header-timestamp-format.json",
            "adds-header-mediatype-format.json",
            "supports-payloads.json",
            "aws-rest-json-uses-jsonname.json",
            "synthesizes-contents.json"
    })

    public void testProtocolResult(String smithy) {
        Model model = Model.assembler()
                .addImport(getClass().getResource(smithy))
                .discoverModels()
                .assemble()
                .unwrap();
        OpenApiConfig config = new OpenApiConfig();
        config.setService(ShapeId.from("smithy.example#Service"));
        ObjectNode result = OpenApiConverter.create()
                .config(config)
                .convertToNode(model);
        String openApiModel = smithy.replace(".json", ".openapi.json");
        InputStream openApiStream = getClass().getResourceAsStream(openApiModel);

        if (openApiStream == null) {
            LOGGER.warning("OpenAPI model not found for test case: " + openApiModel);
        } else {
            Node expectedNode = Node.parse(IoUtils.toUtf8String(openApiStream));
            Node.assertEquals(result, expectedNode);
        }
    }

    @Test
    public void canUseCustomMediaType() {
        Model model = Model.assembler()
                .addImport(getClass().getResource("adds-json-document-bodies.json"))
                .discoverModels()
                .assemble()
                .unwrap();
        OpenApiConfig config = new OpenApiConfig();
        config.setService(ShapeId.from("smithy.example#Service"));
        config.setJsonContentType("application/x-amz-json-1.0");
        OpenApi result = OpenApiConverter.create()
                .config(config)
                .convert(model);

        Assertions.assertTrue(Node.printJson(result.toNode()).contains("application/x-amz-json-1.0"));
    }
}
