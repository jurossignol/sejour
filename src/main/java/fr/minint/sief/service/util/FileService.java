package fr.minint.sief.service.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.minint.sief.domain.Application;
import fr.minint.sief.domain.User;
import fr.minint.sief.repository.ApplicationRepository;
import fr.minint.sief.repository.UserRepository;
import fr.minint.sief.security.SecurityUtils;

/**
 * Service class for managing files.
 */
@Service
public class FileService {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private ApplicationRepository applicationRepository;

    public void loadFile(MultipartFile sourceFile, String destFilePrefix) {
    	
        User currentUser = userRepository.findOneByEmail(SecurityUtils.getCurrentLogin()).get();
        String destFileName = destFilePrefix + "_"
        							+ currentUser.getEmail() + "."
        							+ FilenameUtils.getExtension(sourceFile.getOriginalFilename());
        
        try {
            byte[] bytes = sourceFile.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File("src/main/webapp/assets/fileUpload", destFileName)));
            stream.write(bytes);
            stream.close();
            log.debug( "You successfully uploaded " + destFileName + "!");
        } catch (Exception e) {
        	log.debug( "You failed to upload " + destFileName + " => " + e.getMessage());
        }
	}

    public void loadPhoto(String sourceUri, String destFilePrefix, String idApplication) {
    	
        Application application = applicationRepository.findOne(idApplication);
        String destFileName = destFilePrefix + "_" + application.getEmail() + ".jpeg";
        
        try {
            byte[] bytes = Base64.getDecoder().decode(sourceUri);
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File("src/main/webapp/assets/fileUpload", destFileName)));
            stream.write(bytes);
            stream.close();
            log.debug( "You successfully uploaded " + destFileName + "!");
        } catch (Exception e) {
        	log.debug( "You failed to upload " + destFileName + " => " + e.getMessage());
        }
	}
}
