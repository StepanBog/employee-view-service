package tech.inno.odp.backend.mapper;

import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tech.inno.odp.grpc.generated.documents.DocumentBody;

/**
 * @author VKozlov
 */
@Component
public class CustomDocumentBodyMapper implements DocumentBodyMapper {

    @Autowired
    @Qualifier("delegate")
    private DocumentBodyMapper delegate;

    @Override
    public tech.inno.odp.backend.data.containers.document.DocumentBody transform(DocumentBody documentBody) {
        tech.inno.odp.backend.data.containers.document.DocumentBody body = delegate.transform(documentBody);
        body.setFile(documentBody.getFile().toByteArray());
        return body;
    }

    @Override
    public DocumentBody transform(tech.inno.odp.backend.data.containers.document.DocumentBody documentBody) {
        return delegate.transform(documentBody)
                .toBuilder()
                .setFile(ByteString.copyFrom(documentBody.getFile()))
                .build();
    }
}
