package com.example.adcancedpokedex.data;

import java.lang.reflect.Type;
import java.util.List;

public class Pokemon {

    private String name;
    private List<Stat> stats;
    private List<String> type;
    private String imageUrl;

    public static class Stat {
        private String nameStat;
        private int baseStat;

        public Stat() {}

        public Stat(String nameStat, int baseStat) {
            this.nameStat = nameStat;
            this.baseStat = baseStat;
        }

        public String getNameStat() {
            return nameStat;
        }

        public int getBaseStat() {
            return baseStat;
        }

        public void setNameStat(String nameStat) {
            this.nameStat = nameStat;
        }

        public void setBaseStat(int baseStat) {
            this.baseStat = baseStat;
        }

    }

    public Pokemon (String name, List<Stat> stats, List<String> type, String imageUrl){
        this.name = name;
        this.stats = stats;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public List<String> getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
