package nl.rabobank.kotlinmovement.recipes.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "ingredients")
public class IngredientsEntity {
    @ManyToOne(cascade = javax.persistence.CascadeType.ALL, optional = false)
    @JoinColumn(name="recipes_id", nullable=false)
    private RecipesEntity recipes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private int weight;
}

