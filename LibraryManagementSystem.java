import java.io.*;
import java.util.*;

class Book implements Serializable {
    private String title;
    private String author;
    private boolean issued;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.issued = false;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isIssued() { return issued; }

    public void issueBook() { this.issued = true; }
    public void returnBook() { this.issued = false; }

    @Override
    public String toString() {
        return title + " by " + author + (issued ? " [Issued]" : " [Available]");
    }
}

public class LibraryManagementSystem {
    private static final String FILE_NAME = "library.dat";
    private static ArrayList<Book> books = new ArrayList<>();

    // Save books to file
    private static void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    // Load books from file
    private static void loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            books = (ArrayList<Book>) ois.readObject();
        } catch (Exception e) {
            books = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadBooks();
        int choice;

        do {
            System.out.println("\n--- LIBRARY MENU ---");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: // Add Book
                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    books.add(new Book(title, author));
                    System.out.println("✅ Book added successfully!");
                    saveBooks();
                    break;

                case 2: // View Books
                    System.out.println("\n--- Library Books ---");
                    for (Book b : books) {
                        System.out.println(b);
                    }
                    break;

                case 3: // Issue Book
                    System.out.print("Enter Book Title to Issue: ");
                    title = sc.nextLine();
                    boolean found = false;
                    for (Book b : books) {
                        if (b.getTitle().equalsIgnoreCase(title) && !b.isIssued()) {
                            b.issueBook();
                            System.out.println("📚 Book issued successfully!");
                            found = true;
                            break;
                        }
                    }
                    if (!found) System.out.println("⚠️ Book not available!");
                    saveBooks();
                    break;

                case 4: // Return Book
                    System.out.print("Enter Book Title to Return: ");
                    title = sc.nextLine();
                    found = false;
                    for (Book b : books) {
                        if (b.getTitle().equalsIgnoreCase(title) && b.isIssued()) {
                            b.returnBook();
                            System.out.println("✅ Book returned successfully!");
                            found = true;
                            break;
                        }
                    }
                    if (!found) System.out.println("⚠️ Book was not issued!");
                    saveBooks();
                    break;

                case 5:
                    System.out.println("Exiting... Books saved.");
                    saveBooks();
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        sc.close();
    }
}
