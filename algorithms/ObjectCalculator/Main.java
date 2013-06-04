class C {
    int i;
    int j;
    int k;
}

public class Main {
    public static void main (String[] args) {
        C[] cs = new C[10];
        for (int i=0; i<cs.length; i++) cs[i] = new C();
        System.out.println(ObjectSizeFetcher.getObjectSize(cs));
        int[][] is = new int[10][3];
        System.out.println(ObjectSizeFetcher.getObjectSize(is));
    }
}
