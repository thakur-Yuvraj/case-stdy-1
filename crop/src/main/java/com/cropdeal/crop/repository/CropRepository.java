package com.cropdeal.crop.repository;

import com.cropdeal.crop.model.Crop;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;


@Repository
public interface CropRepository extends JpaRepository<Crop, Integer> {

    @Modifying // i.e insert update delete
    @Transactional // idk delete opr require this annotation
//    @Query("DELETE FROM Crop c WHERE c.Id = :Id")
//    void deleteById(@Param("cropId") int cropId);
    void deleteById(int cropId);

    List<Crop> findByCropNameIgnoreCase(String name);

    List<Crop> findByCropNameAndCropTypeIgnoreCase(String cropName, String cropType);

    // the custom query below, use the upper one
    @Query("SELECT c FROM Crop c WHERE c.cropName = :name AND c.cropType = :type")
    List<Crop> findByNameAndType(@Param("name") String name, @Param("type") String type);

    void deleteByIdAndFarmerId(int cropId, int farmerId);
    // to check the existence of a crop
    boolean existsById(int cropId);
    boolean existsByFarmerId(Integer farmerId);
    boolean existsByIdAndFarmerId(int id, int farmerId);


    List<Crop> findByFarmerId(int farmerId);
}
