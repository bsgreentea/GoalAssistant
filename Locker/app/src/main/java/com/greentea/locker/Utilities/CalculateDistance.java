package com.greentea.locker.Utilities;

public class CalculateDistance {

    public static Double lat = 0.0;
    public static Double lng = 0.0;

    public static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        // kilometer
//        dist = dist * 1.609344;

        // 조정한 meter
//        dist = dist * 160.9344;

        // 원래 meter
        dist = dist*1609.344;
        return (dist);
    }


    // convert decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // convert radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
