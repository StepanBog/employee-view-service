package tech.inno.odp.backend.service.impl;

import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import tech.inno.odp.backend.data.containers.document.DocumentBody;
import tech.inno.odp.backend.data.containers.document.DocumentTemplateGroup;
import tech.inno.odp.backend.mapper.DocumentBodyMapper;
import tech.inno.odp.backend.mapper.DocumentGroupMapper;
import tech.inno.odp.backend.service.IDocumentTemplateService;
import tech.inno.odp.grpc.generated.documentsTemplate.DocumentTemplateGroupSearchRequest;
import tech.inno.odp.grpc.generated.documentsTemplate.DocumentTemplateGroupServiceGrpc;
import tech.inno.odp.grpc.generated.documentsTemplate.DocumentTemplateGroupsResponse;
import tech.inno.odp.grpc.generated.documentsTemplate.FindOneByIdRequest;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 */
@Service
@RequiredArgsConstructor
public class DocumentTemplateService implements IDocumentTemplateService {

    @GrpcClient("odp-document-service")
    private DocumentTemplateGroupServiceGrpc.DocumentTemplateGroupServiceBlockingStub client;

    private final DocumentGroupMapper mapper;
    private final DocumentBodyMapper documentBodyMapper;

    @Override
    public DocumentTemplateGroupsResponse find(@NotNull DocumentTemplateGroupSearchRequest request) {
        return client.find(request);
    }

    @Override
    public DocumentTemplateGroup findById(@NotNull UUID id) {
        return mapper.transform(
                client.findById(
                        FindOneByIdRequest.newBuilder()
                                .setId(id.toString())
                                .build()
                )
        );
    }

    @Override
    public DocumentBody findDocumentFile(UUID documentTemplateId) {
        return documentBodyMapper.transform(
                client.findFileByTemplateId(
                        FindOneByIdRequest.newBuilder()
                                .setId(documentTemplateId.toString())
                                .build())
        );
    }

    @Override
    public void save(@NotNull DocumentTemplateGroup documentTemplateGroup) {
        client.save(
                mapper.transform(documentTemplateGroup)
        );
    }

    @Override
    public void remove(@NotNull DocumentTemplateGroup documentTemplateGroup) {
        client.remove(
                mapper.transform(documentTemplateGroup)
        );
    }

    @Override
    public List<DocumentTemplateGroup> find(Query<DocumentTemplateGroup, DocumentTemplateGroup> query, int pageSize) {
        DocumentTemplateGroupSearchRequest request = mapper.transformToSearch(
                query.getFilter().orElse(null),
                query.getOffset() == 0 ? 0 : query.getOffset() / pageSize,
                pageSize
        );
        DocumentTemplateGroupsResponse response = find(request);
        return mapper.transform(response.getGroupsList());
    }

    @Override
    public int getTotalCount(Query<DocumentTemplateGroup, DocumentTemplateGroup> query) {
        DocumentTemplateGroupSearchRequest request = mapper.transformToSearch(
                query.getFilter().orElse(null),
                0,
                1
        );
        return (int) find(request).getTotalSize();
    }
}
