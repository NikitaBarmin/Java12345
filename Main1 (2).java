import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class Expression {
    public static String[] getExpression() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение, разделяя каждый знак, дробь и скобку пробелом, знак деления = ':' , формат записи дробей = 'q/n' ");
        String userInput = scanner.nextLine();
        if(Objects.equals(userInput, "quit")){
            throw new Exception("Ввод закончен");
        }
        return userInput.split(" ");
    }
}
class Calculator {
    public static Object[] ExpressionToRPN(String[] Expr) {
        ArrayList currentUser = new ArrayList();;
        Stack<String> stack = new Stack<>();
        int priority;
        for (int i =0; i < Expr.length; i++) {
            priority = getPriority(Expr[i]);
            if (priority ==0) currentUser.add(Expr[i]);
            if (priority ==1) stack.push(Expr[i]);
            if (priority > 1) {
                while(!stack.empty()) {
                    if(getPriority(stack.peek()) >= priority)currentUser.add(stack.pop());
                    else break;

                }
                stack.push(Expr[i]);
            }
            if (priority == -1) {
                while (getPriority(stack.peek()) != 1) currentUser.add(stack.pop());
                stack.pop();
            }
        }
        while(!stack.empty())currentUser.add(stack.pop());
        return currentUser.toArray();
    }
    public static String PolkayaKorova(Object[] RPN) throws Exception {
        String operand = new String();
        Stack<String> stack = new Stack<>();
        for(int i = 0; i < RPN.length; i++) {
            if (getPriority(String.valueOf(RPN[i])) == 0){
                operand = (String) RPN[i];
                stack.push(operand);
            }
            if(getPriority(String.valueOf(RPN[i])) > 1){
                drob dr1 = new drob(stack.pop());
                drob dr2 = new drob(stack.pop());
                if (RPN[i].toString().equals("+")) stack.push(drob.summa(dr1,dr2));
                if (RPN[i].toString().equals("-")) stack.push(drob.diff(dr1,dr2));
                if (RPN[i].toString().equals("*")) stack.push(drob.prod(dr1,dr2));
                if (RPN[i].toString().equals(":")) stack.push(drob.quot(dr1,dr2));
            }
        }
        return stack.pop();
    }
    public static int getPriority(String token) {
        if (Objects.equals(token, "*" ) || Objects.equals(token, ":")) return 3;
        else if (Objects.equals(token, "+" ) || Objects.equals(token, "-")) return 2;
        else if (Objects.equals(token, "(" )) return 1;
        else if (Objects.equals(token, ")" )) return -1;
        else return 0;
    }
}
class drob {
    public int chislitel, znamenatel;
    public String drob;
    public drob(String drob)throws Exception {
        int len = drob.length();
        StringBuilder chislitel0 = new StringBuilder();
        StringBuilder znamenatel0 = new StringBuilder();
        String pattern="[0-9]+/[1-9]+|-[0-9]+/[1-9]+";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(drob);
        int index = drob.indexOf('/');
        if (m.matches()) {
            for (int i = 0; i < index; i++) {
                chislitel0.append(drob.charAt(i));
            }
            for (int i = index + 1; i < len; i++) {
                znamenatel0.append(drob.charAt(i));
            }
        } else { throw new Exception("Дробь не распознана");
        }
        this.znamenatel = Integer.parseInt(znamenatel0.toString());
        this.chislitel = Integer.parseInt(chislitel0.toString());
    }
    public static String summa(drob drob0, drob drob1) {
        return (drob0.znamenatel * drob1.chislitel + drob0.chislitel * drob1.znamenatel) + "/" + drob0.znamenatel * drob1.znamenatel;
    }
    public static String diff(drob drob0, drob drob1) {
        return (drob0.znamenatel * drob1.chislitel - drob0.chislitel * drob1.znamenatel) + "/" + drob0.znamenatel * drob1.znamenatel;
    }
    public static String prod(drob drob0, drob drob1) {
        return drob0.chislitel * drob1.chislitel + "/" + drob0.znamenatel * drob1.znamenatel;
    }
    public static String quot(drob drob0, drob drob1) {
        return drob0.chislitel * drob1.znamenatel + "/" + drob0.znamenatel * drob1.chislitel;
    }
}
public class Main1 {
    public static void main(String[] args) throws Exception {
        System.out.println("Дробный калькулятор");
        while (true) {
            System.out.println(Calculator.PolkayaKorova(Calculator.ExpressionToRPN(Expression.getExpression())));
        }
    }
}