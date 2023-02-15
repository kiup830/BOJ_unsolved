import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Vector;


class StrDecoration{
    int N; // 문자열 개수
    String s; //주어진 총 문자열
    String[]str; //요구 문자열을 저장하는 문자열 배열
    int sNum; //요구문자열의 개수
    int strNum[]; // 각 문자열의 문자수

    Vector<String> v=new Vector<String>();
    Vector<Integer> strLength=new Vector<Integer>(); // 체크할 문자열 길이들 저장
    HashMap<String,String> h=new HashMap<>(); //key값에 문자열이 뒤가 겹치는 놈, value는 앞부터 겹치는 놈이다.

    Vector<String> key=new Vector<String>();


    ArrayList<Integer> start=new ArrayList<Integer>();
    ArrayList<Integer> edge=new ArrayList<Integer>();

    public void strCheck(){
        for(int i=0;i<N;i++){
            if(str[i].equals("0")) continue; // 0으로 초기화된 경우 건너뛴다.

            for(int j=i+1;j<N;j++){
                if(str[i].length()>str[j].length()){
                    if(str[i].contains(str[j])){
                        str[j]="0";
                        continue;
                    }
                    strOverlap(i,j);
                }
                else{ //뒤 문자열이 같거나 더 큰 경우
                    if(str[j].contains(str[i])){
                        str[i]="0";
                        break;
                    }
                    strOverlap(j,i); // 문자열이 같은게 있으면 concat해준다.
                }

            }
        }
    }

    public void strOverlap(int num1,int num2){

        int size1=str[num1].length();
        int size2=str[num2].length();

        //사실 둘중 하나의 사이즈가 1인데, 포함되는 경우는 위에서 이미 걸러졌다. 반드시 문자열길이가 2이상이다.
        int i=1; int j=size2-1;
        //str[num1]의 앞과 str[num2]의 뒤가 같을 경우 체크
        if(str[num1].substring(0,i).equals(str[num2].substring(j))){
            while(true)
            {
                i++; j--;
                if(!str[num1].substring(i-1,i).equals(str[num2].substring(j,j+1))){
                    i--; j++; //서로 같던 전 단계로 돌려준다.
                    break;
                }
            }

            h.put(str[num2],str[num1]); //해쉬맵에 String 두개를 넣어줍니다.
            key.add(str[num2]);
            //str[num1]의 중복되는 부분을뺴고 뒤부터 붙여준다.
            str[num2]=str[num2].concat(str[num1].substring(i));
            str[num1]="0";
        }

        int m=1; int n=size1-1;
        //str[num2]의 앞과 str[num1]의 뒤가 같을 경우 체크
        if(str[num2].substring(0,m).equals(str[num1].substring(n))){
            while(true)
            {
                m++; n--;
                if(!str[num2].substring(m-1,m).equals(str[num1].substring(n,n+1))){ //잘못생각했던 부분이 있다. 각글자를 비교해야지 문자열을 비교해버리면 서로 거꾸로라서 다른 값이 나올수 밖에 없다.
                    m--; n++; //서로 같던 전 단계로 돌려준다.
                    break;
                }
            }

            h.put(str[num1],str[num2]); //해쉬맵에 String 두개를 넣어줍니다.
            key.add(str[num1]);
            //str[num2]의 중복되는 부분을뺴고 뒤부터 붙여준다.
            str[num1]=str[num1].concat(str[num2].substring(m));
            str[num2]="0";
        }


    }

    public void strSplit(int num){
        String s1=v.get(num);
        String s2=key.get(0);

        int flag=0;
        for(int i=0;i<key.size();i++)
        {
            if(s1.contains(h.get(key.get(i)))){
                s1=h.get(key.get(i)); //이 녀셕이 더 긴코드 원래에 가까운 코드이다.
                s2=key.get(i);
                flag=1;
            }
        }
       if(flag!=0){
           v.remove(num);
           v.add(num,s1);
           for(int i=0;i<v.size();i++)
           {
               if(v.get(i).equals("0")){
                   v.add(i,s2);
                   v.remove(i+1);
                   strLength.add(i,s2.length());
                   break;
               }
           }
           strLength.remove(num);
           strLength.add(num,s1.length());
       }




    }

    public void strContains(){

        for(int k=0;k<v.size();k++){
            if(v.get(k).equals("0"))
                continue;

            for(int i=s.length(),j=0;i>=strLength.get(k);i--,j++){
                if(s.substring(i-strLength.get(k),s.length()-j).contains(v.get(k))){ //값을 계속 줄여나가면서 저장한 문자열이 있는지 확인.

                    start.add(i-strLength.get(k)); //해당단어의 첫글자의 인덱스를 저장
                    edge.add(s.length()-j-1); //해당 단어의 마지막 글자의 인덱스를 저장 -1까지 해줘야 완성된다.
                    v.remove(k);
                    v.add(k,"0");
                    break;
                }
            }

        }
    }

    public int getMinimumString(){
        int result=0;
         // v벡터에 멀쩡한 문자열을 다 집어넣는다.

        for(int i=0;i<N;i++){
            if(!str[i].equals("0")){
                v.add(str[i]);

            }
        }


        for(int i=0;i<v.size();i++){
            strLength.add(v.get(i).length());
        }




        strContains(); //들어있는지 제대로 확인하는 함수

        for(int k=0;k<v.size();k++){
            if(v.get(k).equals("0"))
                continue;

            strSplit(k); //다 분리해서 다 추가해준다.
        }

        strContains(); //들어있는지 확인하는 함수


        int min=Collections.min(start);
        int max=Collections.max(edge);

        result=max-min+1;
        return result;
    }

    public void run(){
        Scanner sc=new Scanner(System.in);
        int n1=sc.nextInt(); this.N=n1;
        str=new String[N];
        strNum=new int[N];

        for(int i=0;i<N;i++){
            int num=sc.nextInt();
            strNum[i]=num;

            String sp=sc.next();
            str[i]=sp;
        }
        sNum=sc.nextInt();
        s=sc.next();

        strCheck();
        int num=getMinimumString();
        System.out.println(num);
    }
}

public class Main {
    public static void main(String[] args) {
        StrDecoration s1=new StrDecoration();
        s1.run();
    }
}