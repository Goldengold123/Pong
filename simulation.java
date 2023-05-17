import java.util.Scanner;

public class simulation {
    public static void main(String[] args) {
        double incline = 11 * Math.PI / 24;
        Scanner in = new Scanner(System.in);
        System.out.println("THETA: ");
        double theta = in.nextDouble();
        while (true) {
            if (Math.PI <= theta && theta < 3 * Math.PI / 2) {
                // theta = 2 * Math.PI - (Math.PI - incline) * (theta - Math.PI) / incline;
                theta = Math.PI + incline - (Math.PI - incline) * (Math.PI + incline - theta) / incline;
            } else if (3 * Math.PI / 2 <= theta && theta < 2 * Math.PI) {
                // theta = Math.PI + (Math.PI - incline) * (2 * Math.PI - theta) / incline;
                theta = 2 * Math.PI - incline - (Math.PI - incline) * (incline + theta - 2 * Math.PI) / incline;
            } else if (Math.PI / 2 <= theta && theta < Math.PI) {
                // theta = incline * (Math.PI - theta) / (Math.PI - incline);
                theta = incline - incline * (theta - incline) / (Math.PI - incline);
            } else if (0 <= theta && theta < Math.PI / 2) {
                // theta = Math.PI - incline * (theta) / (Math.PI - incline);
                theta = Math.PI - incline + incline * (Math.PI - incline - theta) / (Math.PI - incline);
            } else {
                theta = Math.PI - theta;
            }
            System.out.println(theta);
            System.out.println();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
