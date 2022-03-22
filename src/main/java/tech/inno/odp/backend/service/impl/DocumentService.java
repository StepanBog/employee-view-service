package tech.inno.odp.backend.service.impl;

import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import tech.inno.odp.backend.data.containers.document.Document;
import tech.inno.odp.backend.data.containers.document.DocumentBody;
import tech.inno.odp.backend.mapper.DocumentBodyMapper;
import tech.inno.odp.backend.mapper.DocumentMapper;
import tech.inno.odp.backend.service.IDocumentService;
import tech.inno.odp.grpc.generated.documents.DocumentSearchRequest;
import tech.inno.odp.grpc.generated.documents.DocumentServiceGrpc;
import tech.inno.odp.grpc.generated.documents.DocumentsResponse;
import tech.inno.odp.grpc.generated.documents.FindOneByIdRequest;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 */
@Service
@RequiredArgsConstructor
public class DocumentService implements IDocumentService {

    @GrpcClient("odp-document-service")
    private DocumentServiceGrpc.DocumentServiceBlockingStub client;

    private final DocumentMapper mapper;
    private final DocumentBodyMapper documentBodyMapper;

    @Override
    public DocumentsResponse find(@NotNull DocumentSearchRequest request) {
        return client.find(request);
    }

    @Override
    public DocumentBody findDocumentFile(UUID documentId) {
        return documentBodyMapper.transform(
                client.findFileByDocumentId(
                        FindOneByIdRequest.newBuilder()
                                .setId(documentId.toString())
                                .build())
        );
    }

    @Override
    public List<Document> find(Query<Document, Document> query, int pageSize) {
        DocumentSearchRequest request = mapper.transformToSearch(
                query.getFilter().orElse(null),
                query.getOffset() == 0 ? 0 : query.getOffset() / pageSize,
                pageSize
        );
        DocumentsResponse response = find(request);
        return mapper.transform(response.getDocumentsList());
    }

    @Override
    public int getTotalCount(Query<Document, Document> query) {
        DocumentSearchRequest request = mapper.transformToSearch(
                query.getFilter().orElse(null),
                0,
                1
        );
        return (int) find(request).getTotalSize();
    }
}
