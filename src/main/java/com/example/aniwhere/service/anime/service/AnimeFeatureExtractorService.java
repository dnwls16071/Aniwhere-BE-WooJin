package com.example.aniwhere.service.anime.service;

import com.example.aniwhere.domain.anime.Anime;
import com.example.aniwhere.domain.category.Category;
import com.example.aniwhere.repository.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
public class AnimeFeatureExtractorService {

    private final CategoryRepository categoryRepository;

    public double[] extractFeatures(Anime anime) {
        List<String> allGenres = categoryRepository.findAllCategoryNames();

        double[] genreFeatures = encodeCategories(anime.getCategories(), allGenres);

        double studioFeature = encodeStudio(anime.getStudio());
        double isAdultFeature = anime.getIsAdult() != null && anime.getIsAdult() ? 1.0 : 0.0;
        double episodesFeature = anime.getEpisodesNum() != null ? anime.getEpisodesNum() : 0.0;

        return concat(genreFeatures, new double[]{studioFeature, isAdultFeature, episodesFeature});
    }

    private double[] encodeCategories(Set<Category> categories, List<String> allGenres) {
        double[] genreVector = new double[allGenres.size()];

        for (Category category : categories) {
            int index = allGenres.indexOf(category.getCategoryName());
            if (index >= 0) {
                genreVector[index] = 1.0;
            }
        }
        return genreVector;
    }

    private double encodeStudio(String studio) {
        return studio != null ? studio.hashCode() % 1000 : 0.0;
    }

    private double[] concat(double[]... arrays) {
        int totalLength = 0;
        for (double[] array : arrays) {
            totalLength += array.length;
        }

        double[] result = new double[totalLength];
        int index = 0;
        for (double[] array : arrays) {
            System.arraycopy(array, 0, result, index, array.length);
            index += array.length;
        }
        return result;
    }
}
