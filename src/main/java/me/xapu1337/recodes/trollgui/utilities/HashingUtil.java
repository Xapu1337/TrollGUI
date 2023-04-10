package me.xapu1337.recodes.trollgui.utilities;

public class HashingUtil {
    public static int murmurhash3(byte[] data, int seed) {
        int h1 = seed;
        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;
        final int len = data.length;
        int i = 0;

        while (len - i >= 4) {
            int k1 = (data[i] & 0xff) | ((data[i + 1] & 0xff) << 8) | ((data[i + 2] & 0xff) << 16) | (data[i + 3] << 24);
            k1 *= c1;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= c2;

            h1 ^= k1;
            h1 = Integer.rotateLeft(h1, 13);
            h1 = h1 * 5 + 0xe6546b64;

            i += 4;
        }

        int k1 = 0;
        switch (len - i) {
            case 3:
                k1 ^= (data[i + 2] & 0xff) << 16;
            case 2:
                k1 ^= (data[i + 1] & 0xff) << 8;
            case 1:
                k1 ^= data[i] & 0xff;
                k1 *= c1;
                k1 = Integer.rotateLeft(k1, 15);
                k1 *= c2;
                h1 ^= k1;
        }

        h1 ^= len;
        h1 = fmix(h1);

        return h1;
    }

    private static int fmix(int h) {
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }

}
