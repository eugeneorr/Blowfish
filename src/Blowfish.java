import java.util.ArrayList;

class Blowfish {

    class BlowKey {
        int S[][] = new int[4][256];
        int P[] = new int[18];
    }

    private BlowKey ctx;

    public void generateKey(byte[] key) {
        this.ctx = new BlowKey();

        System.arraycopy(Key.ps, 0, this.ctx.P, 0, 18);
        System.arraycopy(Key.ks0, 0, this.ctx.S[0], 0, 256);
        System.arraycopy(Key.ks1, 0, this.ctx.S[1], 0, 256);
        System.arraycopy(Key.ks2, 0, this.ctx.S[2], 0, 256);
        System.arraycopy(Key.ks3, 0, this.ctx.S[3], 0, 256);

        int data;
        int j = 0, i;
        for (i = 0; i < 18; i++) {
            data = 0x00000000;
            for (int k = 0; k < 4; k++) {
                data = (data << 8) | (key[j] & 0xFF);
                j++;
                if (j >= key.length) j = 0;
            }
            this.ctx.P[i] ^= data;
        }
        byte[] b = new byte[8];
        for (i = 0; i < 18; i += 2) {
            encrypt(b);
            this.ctx.P[i] = toWord(b, 0);
            this.ctx.P[i + 1] = toWord(b, 4);
        }
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 256; j += 2) {
                encrypt(b);
                ctx.S[i][j] = toWord(b, 0);
                ctx.S[i][j + 1] = toWord(b, 4);
            }
        }
    }

    private int F(int x) {
        int a, b, c, d;
        d = x & 0xFF;
        x >>= 8; c = x & 0xFF;
        x >>= 8; b = x & 0xFF;
        x >>= 8; a = x & 0xFF;
        int y = this.ctx.S[0][a] + this.ctx.S[1][b];
        y ^= this.ctx.S[2][c];
        y += this.ctx.S[3][d];
        return y;
    }

    private int toWord(byte[] b, int p) {
        int r = 0;
        r |= b[p + 3] & 0xFF;
        r <<= 8; r |= b[p + 2] & 0xFF;
        r <<= 8; r |= b[p + 1] & 0xFF;
        r <<= 8; r |= b[p] & 0xFF;
        return r;
    }

    private void toBytes(int a, byte[] b, int p) {
        b[p] = (byte) (a & 0xFF);
        a >>= 8; b[p + 1] = (byte) (a & 0xFF);
        a >>= 8; b[p + 2] = (byte) (a & 0xFF);
        a >>= 8; b[p + 3] = (byte) (a & 0xFF);
    }

    void encrypt(byte[] data) {
        int blocks = data.length >> 3;
        for (int k = 0, p; k < blocks; k++) {
            p = k << 3;
            int Xl = toWord(data, p);
            int Xr = toWord(data, p + 4);
            int tmp;
            for (int i = 0; i < 16; i++) {
                Xl = Xl ^ this.ctx.P[i];
                Xr = F(Xl) ^ Xr;
                tmp = Xl;
                Xl = Xr;
                Xr = tmp;
            }
            tmp = Xl;
            Xl = Xr;
            Xr = tmp;
            Xr ^= this.ctx.P[16];
            Xl ^= this.ctx.P[17];
            toBytes(Xl, data, p);
            toBytes(Xr, data, p + 4);
        }
    }

    void decrypt(byte[] data) {
        int blocks = data.length >> 3;
        for (int k = 0, p; k < blocks; k++) {
            p = k << 3;
            int Xl = toWord(data, p);
            int Xr = toWord(data, p + 4);
            int tmp;
            for (int i = 17; i > 1; i--) {
                Xl = Xl ^ this.ctx.P[i];
                Xr = F(Xl) ^ Xr;
                tmp = Xl;
                Xl = Xr;
                Xr = tmp;
            }
            tmp = Xl;
            Xl = Xr;
            Xr = tmp;
            Xr ^= this.ctx.P[1];
            Xl ^= this.ctx.P[0];
            toBytes(Xl, data, p);
            toBytes(Xr, data, p + 4);
        }
    }


    byte[] alignment(byte[] a, int p) {
        int l = (a.length | 7) + 1;
        byte[] b = new byte[l];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) p;
        }
        System.arraycopy(a, 0, b, 0, a.length);
        return b;
    }

    static double calculateCorrelaion(byte[] input, byte[] decrypted) {

        String xbytes = "";
        for (byte b : input) {
            xbytes += (Integer.toBinaryString(b & 255 | 256).substring(1));
        }

        String ybytes = "";
        for (byte b : decrypted) {
            ybytes += (Integer.toBinaryString(b & 255 | 256).substring(1));
        }

        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();

        for (int i = 0; i < xbytes.length(); i++) {
            x.add(Integer.parseInt(String.valueOf(xbytes.charAt(i))));
        }
        for (int i = 0; i < ybytes.length(); i++) {
            y.add(Integer.parseInt(String.valueOf(ybytes.charAt(i))));
        }

            int medianX = getMedian(x);
            int medianY = getMedian(y);
            double nometator = 0;
            double tmpX = 0;
            double tmpY = 0;

            for(int i = 0; i < x.size(); ++i){
                nometator += (x.get(i) - medianX) * (y.get(i) - medianY);
                tmpX += (x.get(i) - medianX) ^ 2;
                tmpY += (y.get(i) - medianY) ^ 2;
            }
            return nometator / Math.sqrt(tmpX * tmpY);
    }

    public static int getMedian(ArrayList<Integer> array) {
        int median;
        if (array.size() % 2 == 0)
            median = array.get(array.size() / 2 - 1);
        else
            median = array.get(array.size() / 2);
        return median;
    }


}
