package ClassManagement;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class ClassManagement {
    private static Map<String, CNTTClass> DanhSachLopHoc = new HashMap<>();

    public static void main(String args[]){
        khoiTaoDuLieu();

        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Danh sach cac lop CNTT:");
            for(String maLop : DanhSachLopHoc.keySet()){
                System.out.println("- " + maLop);
            }
            System.out.println("Nhap ma lop de xem danh sach sinh vien (Hoac bam exit de thoat): ");
            String MaLop = scanner.nextLine();
            if(MaLop.equalsIgnoreCase("exit")){
                break;
            }
            CNTTClass lop = DanhSachLopHoc.get(MaLop);
            if(lop != null){
                lop.showDanhSachSinhVien();
            }
            else{
                System.out.println("Ma lop khong ton tai.");
            }
        }
        scanner.close();
    }
    private static void khoiTaoDuLieu(){
        String[] MaLopArray = {"CNTT1", "CNTT2", "CNTT3", "CNTT4"};
        for(String maLop : MaLopArray){
            DanhSachLopHoc.put(maLop, new CNTTClass("Lop " + maLop, maLop));
        }
        Random rand = new Random();
        String[] LastNameArray = {"Nguyen", "Tran", "Le", "Dang", "Ngo", "Pham", "Hoang", "Vu", "Do"};
        String[] FirstNameArray = {"Minh", "Tuyet", "Chau", "Anh", "Bao", "Duc", "Hieu", "Ngoc", "Khanh", "Nam", "Tuan", "Yen"};
        int day = rand.nextInt(28) + 1;  
        int month = rand.nextInt(12) + 1;  
        int year = 2004;  
        LocalDate birthdate = LocalDate.of(year, month, day);
        for(int i = 0; i < 40; i++){
            String ho = LastNameArray[rand.nextInt(LastNameArray.length)];
            String ten = FirstNameArray[rand.nextInt(FirstNameArray.length)];
            String MaLop = MaLopArray[rand.nextInt(MaLopArray.length)];
            SinhVien sv = new SinhVien(ho, ten, birthdate, 
                                        "Ha Noi", MaLop, Math.round(rand.nextDouble() * 10 * 100.0) / 100.0, Math.round(rand.nextDouble() * 10 * 100.0) / 100.0, 
                                        Math.round(rand.nextDouble() * 10 * 100.0) / 100.0, Math.round(rand.nextDouble() * 10 * 100.0) / 100.0, 
                                        Math.round(rand.nextDouble() * 10 * 100.0) / 100.0);
            DanhSachLopHoc.get(MaLop).addSinhVien(sv);
        }
    }
}


