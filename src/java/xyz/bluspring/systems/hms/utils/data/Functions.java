package xyz.bluspring.systems.hms.utils.data;

public interface Functions {
    interface Function2<C1, C2, T> {
        T create(C1 c1, C2 c2);
    }

    interface Function3<C1, C2, C3, T> {
        T create(C1 c1, C2 c2, C3 c3);
    }

    interface Function4<C1, C2, C3, C4, T> {
        T create(C1 c1, C2 c2, C3 c3, C4 c4);
    }

    interface Function5<C1, C2, C3, C4, C5, T> {
        T create(C1 c1, C2 c2, C3 c3, C4 c4, C5 c5);
    }

    interface Function6<C1, C2, C3, C4, C5, C6, T> {
        T create(C1 c1, C2 c2, C3 c3, C4 c4, C5 c5, C6 c6);
    }

    interface Function7<C1, C2, C3, C4, C5, C6, C7, T> {
        T create(C1 c1, C2 c2, C3 c3, C4 c4, C5 c5, C6 c6, C7 c7);
    }

    interface Function8<C1, C2, C3, C4, C5, C6, C7, C8, T> {
        T create(C1 c1, C2 c2, C3 c3, C4 c4, C5 c5, C6 c6, C7 c7, C8 c8);
    }

    interface Function9<C1, C2, C3, C4, C5, C6, C7, C8, C9, T> {
        T create(C1 c1, C2 c2, C3 c3, C4 c4, C5 c5, C6 c6, C7 c7, C8 c8, C9 c9);
    }

    interface Function10<C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, T> {
        T create(C1 c1, C2 c2, C3 c3, C4 c4, C5 c5, C6 c6, C7 c7, C8 c8, C9 c9, C10 c10);
    }
}
