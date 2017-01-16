package com.prestu.cinemaddict.data.dummy;

public abstract class DummyDataGenerator {

    static String randomFirstName() {
        String[] names = { "Игорь", "Илья", "Катя", "Иван", "Никита",
                "Андрей", "Анна", "Алиса", "Лиза", "Гоша", "Максим", "Паша",
                "Федор", "Марина", "Толя", "Кристина", "Саша", "Наташа", "Даша",
                "Артем", "Сара", "Ольга", "Миша", "Степа" };
        return names[(int) (Math.random() * names.length)];
    }

    static String randomLastName() {
        String[] names = { "Техник", "Хован", "Ресторатор", "Мартышко",
                "Горлышко", "Вагнер", "Целио", "Дартаньян", "Ву", "Тутуруту" };
        return names[(int) (Math.random() * names.length)];
    }

    static String randomCompanyName() {

        String name = randomName();
        if (Math.random() < 0.03) {
            name += " Technologies";
        } else if (Math.random() < 0.02) {
            name += " Investment";
        }
        if (Math.random() < 0.3) {
            name += " Inc";
        } else if (Math.random() < 0.2) {
            name += " Ltd.";
        }

        return name;
    }

    public static String randomWord(int len, boolean capitalized) {
        String[] part = { "ger", "ma", "isa", "app", "le", "ni", "ke", "mic",
                "ro", "soft", "wa", "re", "lo", "gi", "is", "acc", "el", "tes",
                "la", "ko", "ni", "ka", "so", "ny", "mi", "nol", "ta", "pa",
                "na", "so", "nic", "sa", "les", "for", "ce" };
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            String p = part[(int) (Math.random() * part.length)];
            if (i == 0 && capitalized) {
                p = Character.toUpperCase(p.charAt(0)) + p.substring(1);
            }
            sb.append(p);
        }
        return sb.toString();

    }

    static String randomName() {
        int len = (int) (Math.random() * 4) + 1;
        return randomWord(len, true);
    }

}