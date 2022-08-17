import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Calculator {

    public static void main(String[] args) throws NullException, ExpressionException, ArithmeticException {
        boolean A=true;
        Calculator ca = new Calculator();
        Scanner sc = new Scanner(System.in);
        System.out.println("欢迎使用计算器");
        System.out.println("计算请输入带=的算式 ");
        System.out.println("退出请输入over");
        System.out.print("请输入您的需求：");
        Double result;
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            if ("over".equals(str)) {
                System.out.println("谢谢使用~");
                break;
            }
            ca.prepareParam(str);
        }
    }

 /*
 判断算式是否为空，数据是否溢出，构成形式是否正确，拆分字符串
  */
    public void prepareParam(String str) throws NullException, ExpressionException, ArithmeticException {
        if (null == str || "".equals(str)) {
            throw new NullException("没有上传算式");
        }
        if (str.length() > calculate.FORMAT_MAX_LENGTH) {
            throw new ArithmeticException("表达式过长,数据溢出");
        }
        str = str.replaceAll(" ", "");// 去空格
        if ('-' == str.charAt(0)) {// 开头为负数，如-1，改为0-1
            str = 0 + str;
        }
        if (!calculate.check(str)) {
            throw new ExpressionException("表达式错误！");
        }
        // 拆分字符和数字
        String[] nums = str.split("[^.0-9]");
        List<Double> numLst = new ArrayList();
        for (int i = 0; i < nums.length; i++) {
            if (!"".equals(nums[i])) {
                numLst.add(Double.parseDouble(nums[i]));
            }
        }
        String symStr = str.replaceAll("[.0-9]", "");
        System.out.println(operation(symStr, numLst));
    }

    public Double operation(String symStr, List<Double> numLst) throws ArithmeticException {
        LinkedList<Character> spcStack = new LinkedList<>();// 符号栈
        LinkedList<Double> numStack = new LinkedList<>();// 数字栈
        double result = 0;
        int i = 0;// numLst的标志位
        int j = 0;// symStr的标志位
        char sym;
        double num1, num2;// 符号前后两个数
        while (spcStack.isEmpty() || !(spcStack.getLast() == '=' && symStr.charAt(j) == '=')) {
            if (spcStack.isEmpty()) {
                spcStack.add('=');
                numStack.add(numLst.get(i++));
            }
            if (calculate.symLvMap.get(symStr.charAt(j)) > calculate.symLvMap.get(spcStack.getLast())) {// 比较符号优先级，若当前符号优先级大于前一个则压栈
                if (symStr.charAt(j) == '(') {
                    spcStack.add(symStr.charAt(j++));
                    continue;
                }
                numStack.add(numLst.get(i++));
                spcStack.add(symStr.charAt(j++));
            } else {// 当前符号优先级小于等于前一个 符号的优先级
                if (symStr.charAt(j) == ')' && spcStack.getLast() == '(') {// 若（）之间没有符号，则“（”出栈
                    j++;
                    spcStack.removeLast();
                    continue;
                }
                if (spcStack.getLast() == '(') {
                    numStack.add(numLst.get(i++));
                    spcStack.add(symStr.charAt(j++));
                    continue;
                }
                num2 = numStack.removeLast();
                num1 = numStack.removeLast();
                sym = spcStack.removeLast();
                switch (sym) {
                    case '+':
                        numStack.add(calculate.plus(num1, num2));
                        break;
                    case '-':
                        numStack.add(calculate.reduce(num1, num2));
                        break;
                    case '*':
                        numStack.add(calculate.multiply(num1, num2));
                        break;
                    case '/':
                        if (num2 == 0) {// 除数为0
                            throw new ArithmeticException("除数不能为0");
                        }
                        numStack.add(calculate.divide(num1, num2));
                        break;
                }
            }
        }
        return numStack.removeLast();
    }
}
class calculate{
        public static final int FORMAT_MAX_LENGTH = 500;// 表达式最大长度限制
        public static final Map<Character, Integer> symLvMap = new HashMap<Character, Integer>();// 符号优先级map

        static {
            symLvMap.put('=', 0);
            symLvMap.put('-', 1);
            symLvMap.put('+', 1);
            symLvMap.put('*', 2);
            symLvMap.put('/', 2);
            symLvMap.put('(', 3);
            symLvMap.put(')', 1);
        }

        public static boolean check(String str) throws ExpressionException {
            if ('=' != str.charAt(str.length() - 1)) {
                return false;
            }
            if (!(isCharNum(str.charAt(0)) || str.charAt(0) == '(')) {
                throw new ExpressionException("算式结构错误");
            }
            char c;
            for (int i = 1; i < str.length() - 1; i++) {
                c = str.charAt(i);
                if (!isCorrectChar(c)) {// 字符不合法
                    return false;
                }
                if (!(isCharNum(c))) {
                    if (c == '-' || c == '+' || c == '*' || c == '/') {
                        if (c == '-' && str.charAt(i - 1) == '(') {// 1*(-2+3)的情况
                        }
                        if (!(isCharNum(str.charAt(i - 1)) || str.charAt(i - 1) == ')')) {// 若符号前一个不是数字或者“）”时
                            throw new ExpressionException("算式结构错误");
                        }
                    }
                    if (c == '.') {
                        if (!isCharNum(str.charAt(i - 1)) || !isCharNum(str.charAt(i + 1))) {// 校验“.”的前后是否位数字
                            throw new ExpressionException("算式结构错误");
                        }
                    }
                }
            }
            return matches(str);
        }
        //判断括号是否匹配
        public static boolean matches(String str) {
            LinkedList<Character> list = new LinkedList<>();
            for (char c : str.toCharArray()) {
                if (c == '(') {
                    list.add(c);
                } else if (c == ')') {
                    if (list.isEmpty()) {
                        return false;
                    }
                    list.removeLast();
                }
            }
            if (list.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }

        //判断字符是否只是有效字符
        public static boolean isCorrectChar(Character c) {
            if (('0' <= c && c <= '9') || c == '-' || c == '+' || c == '*' || c == '/' || c == '(' || c == ')'
                    || c == '.') {
                return true;
            }
            return false;
        }
        //判断是否为数字
        public static boolean isCharNum(Character c) {
            if (c >= '0' && c <= '9') {
                return true;
            }
            return false;
        }
        //加
        public static double plus(double num1, double num2) {
            return num1 + num2;
        }
        // 减
        public static double reduce(double num1, double num2) {
            return num1 - num2;
        }
        //乘
        public static double multiply(double num1, double num2) {
            return num1 * num2;
        }
        //除
        public static double divide(double num1, double num2) {
            return num1 / num2;
        }

}
class NullException extends Exception{
    public NullException() {
    }

    public NullException(String message) {
        super(message);
    }
}
class ExpressionException extends Exception {
    public ExpressionException() {
    }

    public ExpressionException(String message) {
        super(message);
    }
}
class ArithmeticException extends Exception {
    public ArithmeticException() {
    }

    public ArithmeticException(String message) {
        super(message);
    }
}
