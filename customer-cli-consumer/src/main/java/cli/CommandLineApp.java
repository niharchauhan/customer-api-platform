package cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class CommandLineApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "http://127.0.0.1:64394/api/customers";

    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create Customer");
            System.out.println("2. Get All Customers");
            System.out.println("3. Get Customer by ID");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Exit");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createCustomer();
                case 2 -> getAllCustomers();
                case 3 -> getCustomerById();
                case 4 -> updateCustomer();
                case 5 -> deleteCustomer();
                case 6 -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void createCustomer() throws Exception {
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Middle Name (optional): ");
        String middleName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        String json = objectMapper.writeValueAsString(new CustomerDto(null, firstName, middleName, lastName, email, phone));

        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }
        printResponse(conn);
    }

    private static void getAllCustomers() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }

    private static void getCustomerById() throws Exception {
        System.out.print("Enter Customer ID: ");
        String id = scanner.nextLine();
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }

    private static void updateCustomer() throws Exception {
        System.out.print("Enter Customer ID to update: ");
        String id = scanner.nextLine();

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Middle Name: ");
        String middleName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();

        String json = objectMapper.writeValueAsString(new CustomerDto(UUID.fromString(id), firstName, middleName, lastName, email, phone));

        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }
        printResponse(conn);
    }

    private static void deleteCustomer() throws Exception {
        System.out.print("Enter Customer ID to delete: ");
        String id = scanner.nextLine();
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        conn.setRequestMethod("DELETE");
        printResponse(conn);
    }

    private static void printResponse(HttpURLConnection conn) throws Exception {
        int status = conn.getResponseCode();

        Scanner scanner;
        if (status >= 200 && status < 300) {
            scanner = new Scanner(conn.getInputStream());
        } else {
            scanner = new Scanner(conn.getErrorStream());
            System.out.println("Error: HTTP " + status);
        }

        while (scanner.hasNext()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
    }

    static class CustomerDto {
        public UUID id;
        public String firstName;
        public String middleName;
        public String lastName;
        public String email;
        public String phoneNumber;

        public CustomerDto(UUID id, String fn, String mn, String ln, String email, String phone) {
            this.id = id;
            this.firstName = fn;
            this.middleName = mn;
            this.lastName = ln;
            this.email = email;
            this.phoneNumber = phone;
        }
    }
}
