import java.util.Arrays;

public class Source {
    public static void main(String[] args) {
        Osoba[] tablicaOsob = { new Osoba("Marta", "Nowak"), new Osoba("Adam", "Sosnowski"), new Osoba("Jan", "Nowak"),
                new Osoba("Iza", "Zeno")
        };
        System.out.println(Arrays.toString(tablicaOsob));
        Arrays.sort(tablicaOsob);
        System.out.println(Arrays.toString(tablicaOsob));
    }
}

// public interface Comparable<T> {
// int compareTo(T other);
// }

class Osoba implements Comparable<Osoba> {
    private String imie;
    private String nazwisko;

    public Osoba(String imie, String nazwisko) {
        this.imie = imie;
        this.nazwisko = nazwisko;
    }

    @Override
    public int compareTo(Osoba inna) {
        if (this.imie.compareTo(inna.imie) < 0)
            return 1;
        else if (this.imie.compareTo(inna.imie) > 0)
            return -1;
        else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return imie + " " + nazwisko;
    }
}
