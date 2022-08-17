public class DoubleLinkedList <T>{
    /**
     *
     * 双向链表类
     * 能插入、删除、查找、遍历、翻转链表
     */
    private int key;
    private DoubleLinkedList<T>head;      //头结点
    private DoubleLinkedList<T>tail;      //尾结点
    private DoubleLinkedList<T>next;      //下一个结点的地址
    private DoubleLinkedList<T>previous;       //前一个结点的地址
    private T data;
    private int len;         //长度
    //构造带参函数
    public DoubleLinkedList(int key,T data) {
        this.data = data;
        this.key = key;
        head = this;
        tail = head;
        len++;
    }
    //添加结点
    public void add(DoubleLinkedList<T> node){
        tail.next = node;
        node.previous = tail;
        tail = node;
        len++;
    }
    //删除结点
    public void clear(int key) throws LogicalException {
        //头结点
        if(head.key == key){
            head = head.next;
            head.previous = null;
            len--;
        }else if(tail.key == key){
            tail = tail.previous;
            tail.next = null;
            len--;
        }else{
            DoubleLinkedList<T> c = get(key);
            if(c == null){
                throw new LogicalException("没有该结点");
            }
            c.previous.next = c.next;
            c.next.previous=c.previous;
            c = null;
            len--;
        }
    }
    //根据key查找
    public DoubleLinkedList<T> get(int key){
        if(head.key == key){
            return head;
        }else if(tail.key == key){
            return tail;
        }else{
            DoubleLinkedList<T> temp = head;
            while(temp!=null){
                if(temp.key == key){
                    break;
                }
                temp = temp.next;
            }
            return temp;
        }
    }
    //遍历
    public void show(){
        DoubleLinkedList<T>[] a=new DoubleLinkedList[len];
        a[0]=head;
        for (int i=1;i<len;i++) {
            if (head.next != null) {
                head = head.next;
                a[i]=head;
            }
        }
        for(DoubleLinkedList list:a){
            System.out.println(list);
        }
    }
    //翻转
    public void turn(){
        DoubleLinkedList<T>[] a=new DoubleLinkedList[len];
        a[0]=tail;
        for (int i=1;i<len;i++) {
            if (tail.previous != null) {
                tail = tail.previous;
                a[i]=tail;
            }
        }
        for(DoubleLinkedList list:a){
            System.out.println(list);
        }
}
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public DoubleLinkedList<T> getHead() {
        return head;
    }

    public void setHead(DoubleLinkedList<T> head) {
        this.head = head;
    }

    public DoubleLinkedList<T> getTail() {
        return tail;
    }

    public void setTail(DoubleLinkedList<T> tail) {
        this.tail = tail;
    }

    public DoubleLinkedList<T> getNext() {
        return next;
    }

    public void setNext(DoubleLinkedList<T> next) {
        this.next = next;
    }

    public DoubleLinkedList<T> getPrevious() {
        return previous;
    }

    public void setPre(DoubleLinkedList<T> pre) {
        this.previous = pre;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    @Override
    public String toString() {
        return "DoubleLinkedList [key=" + key + ", data=" + data + "]";
    }
}
/**
 * 测试类
 */
class Test{
    public static void main(String[] args) throws LogicalException {
        DoubleLinkedList<Integer> l = new DoubleLinkedList<Integer>(1, 1);
        for (int i = 2; i < 10; i++) {
            l.add(new DoubleLinkedList<Integer>(i, i));
        }
        l.show();
        System.out.println("---------------------------------");
        l.turn();
        System.out.println("------------------------------");
        System.out.println(l.getHead().getPrevious());
        l.clear(2);
        l.show();
        System.out.println(l.get(7));

    }
}
//自定义异常
class LogicalException extends Exception{
    public LogicalException() {
    }

    public LogicalException(String message) {
        super(message);
    }
}
