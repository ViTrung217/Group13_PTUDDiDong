package ClassManagement;

import java.util.ArrayList;
import java.util.List;

public class CNTTClass {
    private String MaLop;
    private List<SinhVien> DanhSachSinhVien;

    public CNTTClass(String ClassName, String MaLop){
        this.MaLop = MaLop;
        this.DanhSachSinhVien = new ArrayList<>();
    }

    public void addSinhVien(SinhVien sv){
        if(sv.getClassName().equals(this.MaLop)){
            DanhSachSinhVien.add(sv);
        }
        else{
            System.out.println("Sinh vien khong thuoc lop nay.");
        }
    }

    public void showDanhSachSinhVien(){
        int countA = 0, countB = 0, countC = 0, countD = 0, countF = 0;
        for(SinhVien sv : DanhSachSinhVien){
            sv.showInformation();
            System.out.println("*********");
            
            switch(sv.rankScore()){
                case 'A': countA++;
                case 'B': countB++;
                case 'C': countC++;
                case 'D': countD++;
                case 'F': countF++;
            }
        }
        System.out.println("-----TONG KET-----");
        System.out.println("So sinh vien dat hang A la: " + countA);
        System.out.println("So sinh vien dat hang B la: " + countB);
        System.out.println("So sinh vien dat hang C la: " + countC);
        System.out.println("So sinh vien dat hang D la: " + countD);
        System.out.println("So sinh vien dat hang F la: " + countF);
    }

    public String getMaLop(){
        return MaLop;
    }
}
