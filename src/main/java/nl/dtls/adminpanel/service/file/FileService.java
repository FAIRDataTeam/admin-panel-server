package nl.dtls.adminpanel.service.file;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Autowired
    private GridFsOperations gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    public ObjectId storeFile(MultipartFile file) throws IOException {
//        InputStream is = file.getInputStream();
        InputStream is = new FileInputStream("/Users/knaisl/Downloads/logo.png");
        return gridFsTemplate.store(is, "logo.png");
    }

    public GridFsResource getFile(ObjectId fileRef) {
        GridFSFile gridFsdbFile = gridFsTemplate
            .findOne(new Query(Criteria.where("_id").is(fileRef)));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket
            .openDownloadStream(gridFsdbFile.getObjectId());
        return new GridFsResource(gridFsdbFile, gridFSDownloadStream);
    }

}
