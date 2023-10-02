/*
 * Структура хэш-тпблицы
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public class HashMap<K, V> {

    //region Constants
    private static final int INIT_BUCKET_COUNT = 16;
    private static final double LOAD_FACTOR = 0.5;
    //endregion

    //region Fields
    /**
     * Массив бакетов (связныйх списков)
     */
    private Bucket[] buckets;
    private int size;
    //endregion

    //region Constructors
    public HashMap(){
        buckets = new Bucket[INIT_BUCKET_COUNT];
    }

    public HashMap(int index){
        buckets = new Bucket[index];
    }
    //endregion

    //region Public methods

    public V put(K key, V value){
        if (buckets.length * LOAD_FACTOR <= size)
            recalculate();

        int index = calculateBucketIndex(key);
        Bucket bucket = buckets[index];
        if (bucket == null){
            bucket = new Bucket();
            buckets[index] = bucket;
        }

        Entity entity = new Entity();
        entity.key = key;
        entity.value = value;

        V buf = (V)bucket.add(entity);
        if (buf == null){
            size++;
        }
        return buf;
    }

    @Override
    public String toString() {
        StringBuilder toPrint = new StringBuilder();
        for (int i = 0; i < buckets.length; i++){
            Bucket<K, V> bucket = buckets[i];

            String someText;
            if (bucket != null){
                Bucket.Node node = bucket.head;
                while (node != null){
                    someText = "Index: " + i
                            + " Phone: " + node.nodeEntity.key
                            + " Message: " + node.nodeEntity.value
                            + "\n";
                    node = node.next;
                    toPrint.append(someText);
                }
            }
        }
        return toPrint.toString();
    }

    //endregion

    //region Methods

    private void recalculate(){
        size = 0;
        Bucket<K, V>[] old = buckets;
        buckets = new Bucket[old.length * 2];
        for (int i = 0; i < old.length; i++){
            Bucket<K, V> bucket = old[i];
            if (bucket != null){
                Bucket.Node node = bucket.head;
                while (node != null){
                    put((K)node.nodeEntity.key, (V)node.nodeEntity.value);
                    node = node.next;
                }
            }
        }
    }

    private int calculateBucketIndex(K key){
        return Math.abs(key.hashCode()) % buckets.length;
    }

    //endregion

    //region Support structures
    /**
     * Элемент хэш-таблицы
     *
     */
    class  Entity{

        /**
         * Ключ
         */
        K key;

        /**
         * Значение
         */
        V value;
    }

    /**
     * Элемент массива(связный список) из которого состоит ъэш-таблица
     */
    class Bucket<K,V>{

        /**
         * Указатель на первый эллемент сзязного списка
         */
        private Node head;

        /**
         * Узел связного списка
         */
        class Node{
            /**
             * Ссылка на слудующий узел(если имеется)
             */
            Node next;

            /**
             * Значение узла
             */
            Entity nodeEntity;
        }

        public V add(Entity entity){
            Node node = new Node();
            node.nodeEntity = entity;

            if (head == null){
                head = node;
                return null;
            }

            Node currentNode = head;
            while (true) {
                if (currentNode.nodeEntity.key.equals(entity.key)){
                    V temp = (V)currentNode.nodeEntity.value;
                    currentNode.nodeEntity.value = entity.value;
                    return temp;
                }
                if (currentNode.next != null){
                    currentNode = currentNode.next;
                }
                else {
                    currentNode.next = node;
                    return null;
                }
            }
        }
    }
    //endregion
}