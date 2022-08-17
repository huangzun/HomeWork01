import java.util.*;

public class backtracking {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入一个自然数：");
        int goal=sc.nextInt();
        int[] a=new int[100];
        int t=1;
        for (int i=0;i<a.length;i++){
            a[i]=1;
        }
        int m=goal;
        new backtracking().method(a,goal,t,m);
    }
    //回溯法拆分自然数
    public void method(int []a,int goal,int t,int m){
        for (int i=a[t-1];i<=m;i++){    //如果i比自然数小进入循环
          if (i!=goal){     //保证数组里最大的值是自然数-1
              a[t]=i;
              m-=i;
              if (m==0){
                  print(a,t);//出递归，打印数组
              }else {
                  method(a,goal,t+1,m);
              }
              m+=i;    //使上面所有数加起来得到自然数
          }
        }
    }
    //打印结果
    public void print(int[]a,int t){
        for (int i=1;i<t;i++){
            System.out.print(a[i]+"+");
        }
        System.out.println(a[t]);
    }

}