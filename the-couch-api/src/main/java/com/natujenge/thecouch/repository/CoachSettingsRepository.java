package ke.natujenge.baked.repository;

import ke.natujenge.baked.domain.BakerSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BakerSettingsRepository extends JpaRepository<BakerSettings, Long> {
    BakerSettings findByBakerId(Long bakerId);
    BakerSettings findTopByBakerId(Long bakerId);
}
