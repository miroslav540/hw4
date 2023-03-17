public class RedBlackTree {
    private Node root;

    /**
     * Обработка рутовой ноды
     * Если рутовая нода уже существует, то мы создаем новую ноду относительно рута,
     * обязательно сам рут тоже балансируем и назначаем руту черный цвет
     *
     * Если рутового элемента нет, генерируем его и назначаем ему черный цвет
     * (это единственная ситуация, когда мы создаем новую ноду и она НЕ красная)
     */
    public boolean add(int value) {
        if (root != null) {
            boolean result = addNode(root, value);
            root = rebalance(root);
            root.color = Color.BLACK;
            return result;
        } else {
            root = new Node();
            root.color = Color.BLACK;
            root.value = value;
            return true;
        }
    }

    /**
     * Добавление новой ноды
     * Значения ноды уникальны. Не получится создать 2 с одинаковыми значениями
     *
     * Если значение ноды больше искомого значения, при этом левый ребенок существует, запускаем
     * рекурсивный поиск в глубину по левому ребенку на предмет можно ли создать новую ноду там
     *
     * Если левой ноды не существует, считаем что подходящее место для создания новой ноды найдено, присваиваем ей красный цвет
     *
     * Если правого ребенка нет, то генерируем значение.
     * Если правый ребенок на месте, запускаем рекурсивный поиск в глубину по правому ребенку на предмет создания там новой ноды
     */
    private boolean addNode(Node node, int value) {
        if (node.value == value) {
            return false;
        } else {
            if (node.value > value) {
                if (node.leftChild != null) {
                    boolean result = addNode(node.leftChild, value);
                    node.leftChild = rebalance(node.leftChild);
                    return result;
                } else {
                    node.leftChild = new Node();
                    node.leftChild.color = Color.RED;
                    node.leftChild.value = value;
                    return true;
                }
            } else {
                if (node.rightChild != null) {
                    boolean result = addNode(node.rightChild, value);
                    node.rightChild = rebalance(node.rightChild);
                    return result;
                } else {
                    node.rightChild = new Node();
                    node.rightChild.color = Color.RED;
                    node.rightChild.value = value;
                    return true;
                }
            }
        }
    }

    /**
     * Ребалансировка
     * Если есть правый ребенок красного цвета, при этом левый ребенок черного цвета,
     * тогда мы производим правый поворот и говорим, что балансировка д.б. выполнена ещё раз;
     *
     * Если есть левый ребенок красного цвета, при этом у него есть
     * свой левый ребенок красного цвета, тогда мы производим левый поворот;
     *
     * Если и правый и левый дети имеют красный цвет, мы выполняем colorSwap.
     *
     * Как только условия перестанут выполняться, выйдем из цикла и получим результат
     */
    private Node rebalance(Node node) {
        Node result = node;
        boolean needRebalance;
        do {
            needRebalance = false;
            if (result.rightChild != null && result.rightChild.color == Color.RED &&
                    (result.leftChild == null || result.leftChild.color == Color.BLACK)) {
                needRebalance = true;
                result = rightSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.RED &&
                    result.leftChild.leftChild != null && result.leftChild.leftChild.color == Color.RED) {
                needRebalance = true;
                result = leftSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.RED &&
                    result.rightChild != null && result.rightChild.color == Color.RED) {
                needRebalance = true;
                colorSwap(result);
            }
        }
        while (needRebalance);
        return result;
    }

    /**
     * Правосторонний малый поворот
     */
    private Node rightSwap(Node node) {
        Node rightChild = node.rightChild;          // берем правого ребенка, выделяем как отдельную переменную
        Node betweenChild = rightChild.leftChild;   // берем промежуточного ребенка, это тот элемент, который будет менять своего родителя
        rightChild.leftChild = node;                // вместо левого ребенка красной ноды назначаем рутовый элемент с которого начали (текущего родителя)
        node.rightChild = betweenChild;             // у родителя правым элементом становится промежуточный ребенок (между красной и рутовой нодами)
        rightChild.color = node.color;              // правый ребенок получает цвет своего родителя
        node.color = Color.RED;                     // корень, который опустился ниже, стал красным
        return rightChild;
    }

    /**
     * Левосторонний малый поворот
     */
    private Node leftSwap(Node node) {
        Node leftChild = node.leftChild;            // берем левого ребенка, выделяем как отдельную переменную
        Node betweenChild = leftChild.rightChild;   // берем промежуточного ребенка, это тот элемент, который будет менять своего родителя
        leftChild.rightChild = node;                // вместо правого ребенка красной ноды назначаем рутовый элемент с которого начали (текущего родителя)
        node.leftChild = betweenChild;              // у родителя левым элементом становится промежуточный ребенок (между красной и рутовой нодами)
        leftChild.color = node.color;               // левый ребенок получает цвет своего родителя
        node.color = Color.RED;                     // корень, который опустился ниже, стал красным
        return leftChild;
    }

    /**
     * Смена цвета
     * Если нужно произвести смену цвета указанной ноды, мы присваиваем её детям
     * (левому и правому) чёрные цвета, а сама нода становится красной
     * Ситуация возможна только когда у ноды два красных ребенка
     */
    private void colorSwap(Node node) {
        node.rightChild.color = Color.BLACK;
        node.leftChild.color = Color.BLACK;
        node.color = Color.RED;
    }
    private class Node {
        private int value;
        private Color color;
        private Node leftChild;
        private Node rightChild;

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", color=" + color +
                    '}';
        }
    }

    private enum Color {
        RED, BLACK
    }
}
