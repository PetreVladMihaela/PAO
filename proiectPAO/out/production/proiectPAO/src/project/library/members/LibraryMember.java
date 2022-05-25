package project.library.members;

import project.library.books.Author;
import project.library.books.Book;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LibraryMember implements Comparable<LibraryMember> {
    private int id;
    private String name;
    private String email;
    private String phone;
    private Address address;
    private LinkedList<Book> borrowedBooks;
    private LocalDate membershipStarted;
    private LocalDate membershipExpires;
    private int maxBooksAllowed = 3;

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public void setMaxBooksAllowed(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LinkedList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(LinkedList<Book> borrowedBooks) { this.borrowedBooks = borrowedBooks; }

    public LocalDate getMembershipStarted() {
        return membershipStarted;
    }

    public void setMembershipStarted(LocalDate membershipStarted) {
        this.membershipStarted = membershipStarted;
    }

    public LocalDate getMembershipExpires() {
        return membershipExpires;
    }

    public void setMembershipExpires(LocalDate membershipExpires) {this.membershipExpires = membershipExpires; }

    public void borrowBook(Book book) {
        int copies = book.getCopiesInLibrary();
        if (copies > 0) {
            this.borrowedBooks.add(book);
            book.setCopiesInLibrary(copies-1);
        }
        else
            System.out.println("There are no more copies of "+book.getTitle()+" in the library.");
    }

    public void returnBooks(Book... returnedBooks) {
        for (Book book : returnedBooks) {
            this.borrowedBooks.remove(book);
            int copies = book.getCopiesInLibrary();
            book.setCopiesInLibrary(copies+1);
        }
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        borrowedBooks.forEach(book -> { joiner.add("Book{Title='"+book.getTitle());
            if (nonNull(book.getAuthors()))
                joiner.add("Authors="+book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()));
            else joiner.add("Authors="+null);
            joiner.add("CopiesInLibrary=" + book.getCopiesInLibrary()+"}");
        });
        return "LibraryMember{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", address=" + address + ",\n" +
                "  borrowedBooks=" + joiner + ",\n" +
                "  membershipStarted=" + membershipStarted +
                ", membershipExpires=" + membershipExpires +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LibraryMember)) return false;
        LibraryMember member = (LibraryMember) o;
        return //getId() == member.getId() &&
                Objects.equals(getName(), member.getName()) &&
                Objects.equals(getEmail(), member.getEmail()) &&
                Objects.equals(getPhone(), member.getPhone());
    }

    @Override
    public int compareTo(LibraryMember o) {
        if (this.equals(o)) {
            return 0;
        }
        else return this.id - o.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getPhone());
    }

    public static LibraryMemberBuilder newMember() {
        return new LibraryMemberBuilder();
    }

    public static class LibraryMemberBuilder {
        //private int id;
        private String name = "anonymous";
        private String email = "-";
        private String phone = "-";
        private Address address;
        private LinkedList<Book> borrowedBooks = new LinkedList<>();
        private LocalDate membershipStarted = LocalDate.now();
        private LocalDate membershipExpires = LocalDate.now().plusMonths(1);

        public LibraryMemberBuilder name(String name) {
            this.name = name;
            return this;
        }

        public LibraryMemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public LibraryMemberBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public LibraryMemberBuilder address(Address address) {
            this.address = address;
            return this;
        }

        public LibraryMemberBuilder borrowedBooks(Book... borrowedBooks) {
            this.borrowedBooks = new LinkedList<>(Arrays.asList(borrowedBooks));
            return this;
        }

        public LibraryMemberBuilder membershipStarted(LocalDate membershipStarted) {
            this.membershipStarted = membershipStarted;
            return this;
        }

        public LibraryMemberBuilder membershipExpires(LocalDate membershipExpires) {
            this.membershipExpires = membershipExpires;
            return this;
        }

        public LibraryMember buildMember() {
            LibraryMember member = new LibraryMember();
            member.setName(this.name);
            member.setEmail(this.email);
            member.setPhone(this.phone);
            member.setAddress(this.address);
            member.setBorrowedBooks(this.borrowedBooks);
            member.setMembershipStarted(this.membershipStarted);
            member.setMembershipExpires(this.membershipExpires);
            return member;
        }
    }
}
