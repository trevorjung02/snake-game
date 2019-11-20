import java.util.*;

class SnakeGame {
	public static void main(String[] args) {
		//Body should be given head first
		System.out.println("Use the keys W,A,S,D and press enter to move. Eat the asterisks to grow your snake body and increase your score. Don't hit yourself or the walls.");
		Point[] body = new Point[] {new Point(0,0)};
		Game g = new Game(10, 10, new ArrayList<Point>(Arrays.asList(body)));
		g.playGame();
	}
}

class Game {
	char[][] board;
	int width;
	int height;
	Snake s;
	Apple a;

	public Game(int width, int height, List<Point> body) {
		this.width = width;
		this.height = height;
		board = new char[width][height];
		s = new Snake(body, Direction.DOWN);
		a = new Apple(randomLocation());
	}

	public void playGame() {
		int score = 0;
		render();
		while(true) {
			s.setDirection(getPlayerDirection(s.dir));
			s.takeStep(a.location);
			if(s.getHead().equals(a.location)) {
				score++;
				if(victory()) {
					render();
					System.out.println("You won");
					System.out.println("Score: " + score);
					return;
				}
				a.setLocation(randomLocation());
			}
			render();
			if(!snakeAlive()) {
				System.out.println("You died");
				System.out.println("Score: " + score);
				return;
			}
		}
	}

	public boolean victory() {
		if(s.body.size() == width*height) {
			return true;
		}
		return false;
	}

	public Point randomLocation() {
		Random rng = new Random();
		Point curPoint = new Point(rng.nextInt(width),rng.nextInt(height));
		while(s.body.contains(curPoint)) {
			int x = rng.nextInt(width);
			int y = rng.nextInt(height);
			curPoint = new Point(x,y);
		}
		return curPoint;
	}

	public boolean snakeAlive() {
		if(s.alive == false) {
			return false;
		}
		Point sHead= s.getHead();
		if(sHead.x >= width || sHead.x < 0 || sHead.y >= height || sHead.y < 0) {
			return false;
		}
		return true;
	}

	public void render() {
		for(int i = 0; i < width+2; i++) {
			System.out.print("-");
		}
		System.out.println();

		for(int i = 0; i < height; i++) {
			System.out.print("|");
			for(int j = 0; j < width; j++) {
				Point curPoint = new Point(j,i);
				if(s.body.contains(curPoint)) {
					if(s.getHead().equals(curPoint)) {
						System.out.print("X");
					}
					else {
						System.out.print("O");
					}
				}
				else if(curPoint.equals(a.location)) {
					System.out.print("*");
				}
				else {
					System.out.print(board[j][i]);
				}
			}
			System.out.println("|");
		}

		for(int i = 0; i < width+2; i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	public Direction getPlayerDirection(Direction cur) {
		Scanner scan = new Scanner(System.in);
		Direction newDir = cur;
		if(scan.hasNext()) {
			String input = scan.next();
			switch(input.toLowerCase()) {
				case "w": if(cur != Direction.DOWN) newDir = Direction.UP; break;
				case "a": if(cur != Direction.RIGHT) newDir = Direction.LEFT; break;
				case "s": if(cur != Direction.UP) newDir = Direction.DOWN; break;
				case "d": if(cur != Direction.LEFT) newDir = Direction.RIGHT; break;
			}
		}
		return newDir;
	}
}

class Snake {
	Set<Point> body;
	Queue<Point> order;
	Direction dir; 
	Point head;
	boolean alive;

	public Snake(Point start, Direction dir) {
		body = new HashSet<>();
		order = new LinkedList<>();
		head = start;
		body.add(start);
		order.offer(start);
		this.dir = dir;
		alive = true;
	}

	public Snake(List<Point> body, Direction dir) {
		this.body = new HashSet<Point>();
		order = new LinkedList<>();
		head = body.get(0);
		for(int i = body.size()-1; i >= 0; i--) {
			this.body.add(body.get(i));
			order.offer(body.get(i));
		}
		this.dir = dir;
		alive = true;
	}

	public void takeStep(Point apple) {
		head = new Point(head.x+dir.getX(), head.y+dir.getY());
		if(body.contains(head) && !head.equals(order.peek())) {
			alive = false;
		}
		if(!head.equals(apple)) {
			body.remove(order.poll());
		}
		body.add(head);
		order.add(head);
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public Point getHead() {
		return head;
	}
}

enum Direction {
	UP(0,-1), DOWN(0,1), LEFT(-1,0), RIGHT(1,0);

	private final int x;
	private final int y;

	private Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}

class Point {
	int x;
	int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		Point other = (Point) obj;
		return x==other.x && y==other.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}

class Apple {
	Point location;

	public Apple(Point location) {
		this.location = location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
}