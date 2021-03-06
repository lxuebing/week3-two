package com.tw;

import java.util.*;

/*
 * This Java source file was generated by the Gradle 'init' task.
 * 张三,1,数学:90,英语:80,物理:79
 * 李四,2,数学:95,英语:73,物理:89
 * 王五,3,数学:88,英语:70,物理:60
 */
public class Library {
    Scanner sc = new Scanner(System.in);
    Map<Long, Student> students = new HashMap<>();
    static Set<String> subNames = new HashSet<>();
    public boolean someLibraryMethod() {

        while (true) {
            System.out.println("1. 添加学生\n2. 生成成绩单\n3. 退出\n请输入你的选择（1～3）：");
            int n = sc.nextInt();
            switch (n){
                case 1:
                    this.addStudent();
                    break;
                case 2:
                    this.scoreRecord();
                    break;
                case 3:
                    return true;
            }
        }
    }

    public void addStudent() {
        System.out.println("请输入学生信息（格式：姓名, 学号, 民族, 班级, 学科: 成绩, ...），按回车提交：");
        String str = sc.next();
        try {
            Student student = Student.create(str);
            students.put(student.id, student);
            System.out.println(String.format("学生%s的成绩被添加", student.name));
        }catch (Exception e){
            System.out.println("请按正确的格式输入（格式：姓名, 学号, 民族, 班级, 学科: 成绩, ...）：");
        }

    }

    public void scoreRecord(){
        System.out.println("请输入要打印的学生的学号（格式： 学号, 学号,...），按回车提交：");
        try {
            String[] id_str = sc.next().split(",");
            Set<Long> ids = new HashSet<>();

            for (int i = 0; i < id_str.length; i++) ids.add(Long.parseLong(id_str[i]));
            System.out.println("成绩单");
            System.out.print("姓名");
            System.out.print("民族");
            System.out.print("班级");
            for (String subName : subNames) System.out.print("|" + subName);
            System.out.println("|平均分|总分");
            System.out.println("========================");
            for (Long id : ids){
                if (students.containsKey(id)){
                    Student student = students.get(id);
                    System.out.print(student.name);
                    System.out.print(student.nation);
                    System.out.print(student.klass);
                    for (String subName: subNames){
                        float score = 0;
                        if (student.subjects.containsKey(subName)) score = student.subjects.get(subName);
                        System.out.print("|" + score);
                    }
                    System.out.println("|"+ student.avg +"|" + student.sum);
                }
            }
            System.out.println("========================");

            float[] sumList = new float[students.size()];
            int i = 0;
            float sum = 0;
            for (Student student: students.values()){
                sumList[i++] = student.sum;
                sum += student.sum;
            }
            System.out.println("全班总分平均数：" + (sum/students.size()));
            System.out.println("全班总分中位数：" + this.getMid(sumList));

        }catch (Exception e){
            System.out.println("请按正确的格式输入要打印的学生的学号（格式： 学号, 学号,...），按回车提交：");
        }


    }

    public float getMid(float[] arr){
        Arrays.sort(arr);
        int l = arr.length;
        if (l == 1) return arr[0];
        if (l == 2) return arr[0] + arr[1];
        if (l % 2 != 0) return arr[l/2];

        return (arr[l] + arr[l-1]) /2;
    }
}

class Student{
    //姓名, 学号, 民族, 班级, 学科: 成绩

    String name;
    long id;
    String nation;
    String klass;
    Map<String, Float> subjects;
    float avg;
    float sum;

    Student(String name, long id, String nation, String klass, Map<String, Float> subjects){
        this.name = name;
        this.id = id;
        this.nation = nation;
        this.klass = klass;
        this.subjects = subjects;

        float sum = 0;
        for (Float value : subjects.values()) {
            sum += value;
        }

        if(this.nation != "汉族"){
            sum += 10;
        }
        if(this.klass == "艺术"){
            sum += 15;
        }
        if(this.klass == "体育"){
            sum += 20;
        }

        this.avg = sum / subjects.size();
        this.sum = sum;
    }

    @Override
    public boolean equals(Object obj) {
        Student student = (Student) obj;
        if(this.id == student.id) return true;
        return false;
    }

    public static Student create(String info) throws Exception{
        String[] strs = info.split(",");
        if (strs.length < 5) throw new Exception("格式错误");

        if (strs[0].equals("")) throw new Exception("name错误");

        String name = strs[0];
        String nation = strs[2];
        String klass = strs[3];

        long id = Long.parseLong(strs[1]);

        Map<String, Float> subjects = new HashMap<>();

        for (int i = 2; i < strs.length; i++){
            String[] tmp = strs[i].split(":");
            if(tmp.length != 2) throw new Exception("tmp错误");
            String sub_name = tmp[0];
            float sub_score = Float.parseFloat(tmp[1]);
            subjects.put(sub_name, sub_score);
            Library.subNames.add(sub_name);
        }
        return new Student(name, id, nation, klass, subjects);
    }
}

