package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.archive.Archive;
import it.polito.ai.lab03.repository.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.List;

@SuppressWarnings("ALL")
public interface ArchiveRepository extends MongoRepository<Archive, User> {
    Archive insert(@NonNull Archive archive);
    Archive findArchiveById(@NonNull String id);
    List<Archive> findArchivesByUserIdAndOnSale(@NonNull String userId, @NonNull boolean onSale);
    List<Archive> deleteArchiveByUserId(@NonNull String userId);
}
