package socialnetwork.domain;


/**
 * Define a Pereche o generic type entities
 * @param <E1> - pereche first entity type
 * @param <E2> - pereche second entity type
 */
public class Pereche<E1,E2> extends Tuple<E1,E2>{

    public Pereche(E1 e1, E2 e2) {
        super(e1, e2);
    }

    @Override
    public boolean equals(Object obj) {
        return (this.e1.equals(((Pereche) obj).e1) && this.e2.equals(((Pereche) obj).e2)) ||
                (this.e1.equals(((Pereche) obj).e2) && this.e2.equals(((Pereche) obj).e1));
    }

}