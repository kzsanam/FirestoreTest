package org.example;

import java.util.List;

public class City {
        public String name;
        public String state;
        public String country;
        public Boolean capital;
        public Long population;
        public List<String> regions;
        // Must have a public no-argument constructor

// Initialize all fields of a city
        public City(
                String name,
                String state,
                String country,
                Boolean capital,
                Long population,
                List<String> regions) {
                this.name = name;
                this.state = state;
                this.country = country;
                this.capital = capital;
                this.population = population;
                this.regions = regions;
        }
}
