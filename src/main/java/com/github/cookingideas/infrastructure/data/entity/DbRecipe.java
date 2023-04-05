package com.github.cookingideas.infrastructure.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity(name = "recipe")
public class DbRecipe {
    @Id
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "recipe", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private List<DbIngredient> ingredients;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DbIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<DbIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
