package ClassManagement;

import java.time.LocalDate;

public class SinhVien{
    private String FirstName;
    private String LastName;
    private LocalDate BirthDate;
    private String Address;
    private String ClassName;
    private double OOPScore;
    private double ProjectManagementScore;
    private double MachineLearningScore;
    private double DatabaseScore;
    private double MobileDevelopmentScore;

    public SinhVien(String FirstName, String LastName, LocalDate BirthDate,
                    String Address, String ClassName, double OOPScore, 
                    double ProjectManagementScore, double MachineLearningScore,
                    double DatabaseScore, double MobileDevelopmentScore){
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.BirthDate = BirthDate;
        this.Address = Address;
        this.ClassName = ClassName;
        this.OOPScore = OOPScore;
        this.ProjectManagementScore = ProjectManagementScore;
        this.MachineLearningScore  = MachineLearningScore;
        this.DatabaseScore = DatabaseScore;
        this.MobileDevelopmentScore = MobileDevelopmentScore;
    }

    public double averageScore(){
        return ((OOPScore + ProjectManagementScore + MachineLearningScore +
                DatabaseScore + MobileDevelopmentScore) / 5);
    }

    public char rankScore(){
        double dtb = averageScore();
        if(dtb >= 8.5) return 'A';
        else if(dtb >= 7.0) return 'B';
        else if(dtb >= 5.5) return 'C';
        else if(dtb >= 4.0) return 'D';
        else return 'F';
    }

    public void showInformation(){
        System.out.println("Ho va ten: " + FirstName + " " + LastName);
        System.out.println("Ngay sinh: " + BirthDate);
        System.out.println("Dia chi: " + Address);
        System.out.println("Lop: " + ClassName);
        System.out.println("Diem Lap trinh huong doi tuong: " + OOPScore);
        System.out.println("Diem Quan ly du an: " + ProjectManagementScore);
        System.out.println("Diem Hoc may:" + MachineLearningScore);
        System.out.println("Diem Co so du lieu: " + DatabaseScore);
        System.out.println("Diem Lap trinh ung dung cho TBDD: " + MobileDevelopmentScore);
        System.out.println("Diem trung binh: " + String.format("%.2f", averageScore()));
        System.out.println("Hang: " + rankScore());
    }

    public String getClassName(){
        return ClassName;
    }
}
