package com.atlassian.jsm.spikes.jsonstreaming;

import java.util.List;

public class Model {
    List<Item> items;

    public static class Item {
        String key;
        Long number1;
        Float number2;
        List<Integer> elements;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Long getNumber1() {
            return number1;
        }

        public void setNumber1(Long number1) {
            this.number1 = number1;
        }

        public Float getNumber2() {
            return number2;
        }

        public void setNumber2(Float number2) {
            this.number2 = number2;
        }

        public List<Integer> getElements() {
            return elements;
        }

        public void setElements(List<Integer> elements) {
            this.elements = elements;
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
