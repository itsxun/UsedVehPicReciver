package xin.fallen.domain;

/**
 * Author: Fallen
 * Date: 2017/4/5
 * Time: 14:15
 * Usage:
 */
public class Fallen {

    private String pic;
    private String jylsh;
    private String zplx;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getJylsh() {
        return jylsh;
    }

    public void setJylsh(String jylsh) {
        this.jylsh = jylsh;
    }

    public String getZplx() {
        return zplx;
    }

    public void setZplx(String zplx) {
        this.zplx = zplx;
    }

    @Override
    public String toString() {
        return "Fallen{" +
                "pic='" + pic + '\'' +
                ", jylsh='" + jylsh + '\'' +
                ", zplx='" + zplx + '\'' +
                '}';
    }
}
