import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

/**
 * @author RChen Date: Oct. 31, 2017
 */
public class KdTree {

	private Node root;
	private int size;

	private static class Node {
		private Point2D p;		// the point
		private RectHV rect;	// the axis-aligned rectangle corresponding to this node
		private Node lb;		// the left/bottom subtree
		private Node rt;		// the right/top subtree
		
		public Node(Point2D p) {
			this.p = p;
		}
	}

	// construct an empty set of points 
	public KdTree() {
		root = null;
		size = 0;
	}
	
	// is the set empty?
	public boolean isEmpty() {
		return root == null;
	}

	// number of points in the set
	public int size() {
		return size;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		// corner cases
		if (p == null) throw new IllegalArgumentException("Calls insert() with a null point");
		
		Node inputNode = new Node(p);
		if (root == null) {
			inputNode.rect = new RectHV(0, 0, 1, 1);
			root = inputNode;
			size++;
		} else {
			// true refers to even levels, false refers to odd levels
			root = insert(root, inputNode, true);			
		}
	}

	private Node insert(Node n, Node p, boolean isEven) {
		if (n == null) {
			size++;
			return p;
		} else if (n.p.equals(p.p)) {
			// return n but not change size since it is duplicate
			return n;
		}
		if (isEven) {
			// if the point to be inserted has a smaller x-coordinate than the point at the root, go left; 
			// otherwise go right
			if (p.p.x() < n.p.x()) {
				p.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
				n.lb = insert(n.lb, p, !isEven);
			} else {
				p.rect = new RectHV(n.p.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
				n.rt = insert(n.rt, p, !isEven);
			}
		} else {
			// if the point to be inserted has a smaller y-coordinate than the point in the node, go left; 
			// otherwise go right
			if (p.p.y() < n.p.y()) {
				p.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
				n.lb = insert(n.lb, p, !isEven);
			} else {
				p.rect = new RectHV(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.rect.ymax());
				n.rt = insert(n.rt, p, !isEven);
			}
		}
		return n;
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null) throw new IllegalArgumentException("argument to contains() is null");
		return contains(root, p, true);
	}

	private boolean contains(Node n, Point2D p, boolean isEven) {
		if (n == null) {
			return false;
		} else if (n.p.equals(p)) {
			return true;
		}
		if (isEven) {
			// if the point has a smaller x-coordinate than the point at the root, go left; 
			// otherwise go right
			if (p.x() < n.p.x()) {
				return contains(n.lb, p, !isEven);
			} else {
				return contains(n.rt, p, !isEven);
			}
		} else {
			// if the point has a smaller y-coordinate than the point in the node, go left; 
			// otherwise go right
			if (p.y() < n.p.y()) {
				return contains(n.lb, p, !isEven);
			} else {
				return contains(n.rt, p, !isEven);
			}
		}
	}

	// draw all points to standard draw
	public void draw() {
		
	} 

	// all points that are inside the rectangle (or on the boundary)
//	public Iterable<Point2D> range(RectHV rect) {}              

	// a nearest neighbor in the set to point p; null if the set is empty
//	public Point2D nearest(Point2D p) {} 

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		KdTree tree = new KdTree();
		Point2D p1 = new Point2D(0, 0.1);
		System.out.println("tree initialization size: " + tree.size());
		tree.insert(p1);
		System.out.println("tree p1 size: " + tree.size());
		System.out.println("tree contains p1: " + tree.contains(p1));
		System.out.println("tree contains new p1: " + tree.contains(new Point2D(0, 0.1)));
		tree.insert(p1);
		System.out.println("tree p1 size: " + tree.size());
	}
}
