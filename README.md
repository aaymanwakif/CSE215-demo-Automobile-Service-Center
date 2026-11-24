# Automobile Service Center Management System

A beginner-friendly Java Swing application for managing the daily operations of an automobile workshop.

## Features

- Register customers and their vehicles
- Add mechanics with basic salary and per-job performance bonus
- View built-in service packages (Basic, Full, and Engine Service)
- Manage spare-part stock and unit prices
- Create service records and calculate invoices
- Automatically calculate loyalty discounts:
  - Regular: 0% (fewer than 5 completed services)
  - Returning: 5% (5–9 completed services)
  - VIP: 10% (10 or more completed services)
- Calculate mechanic salary from basic salary plus completed-job bonuses
- View service history and a business summary
- Automatically save data locally between runs

All money values are in Bangladeshi Taka (Tk).

## Requirements

- JDK 17 or newer
- VS Code with the Java Extension Pack (or any Java IDE)

## Run the Swing interface

Run `autoservice.Main` from your IDE to open the desktop window. The application stores records in `service-center-data.csv` in the working directory. Existing `service-center-data.ser` files are imported automatically the first time.

### Configure the owner login

Create `owner-credentials.properties` in the project folder and set your local owner credentials:

```properties
owner.email=your-owner-email
owner.password=your-owner-password
```

The file is excluded by `.gitignore`, so it stays out of Git uploads. It is plain text on your computer, not encrypted; protect access to the computer and never commit or upload the file. The app reads it when you open the owner login. Customer passwords are stored as salted hashes in the CSV, not as readable passwords. Existing customers without a login can create an account using the same name and email already on file.

### Compile and launch manually (optional)

### Windows PowerShell

```powershell
javac -d out src\autoservice\*.java
java -cp out autoservice.Main
```

### macOS / Linux

```bash
mkdir -p out
javac -d out src/autoservice/*.java
java -cp out autoservice.Main
```

The CSV is plain text, so you can inspect it with a text editor or spreadsheet program. Each row starts with a record type (`CUSTOMER`, `VEHICLE`, `MECHANIC`, `PACKAGE`, `PART`, or `RECORD`), followed by that record's fields.

## Suggested demo flow

1. Register a customer.
2. Register a vehicle for that customer.
3. Add a mechanic.
4. Add spare parts and stock.
5. Create a service record, select a package, and use spare parts.
6. Show the generated invoice and service history.
7. Open mechanic payroll and the business summary.

## Project structure

```text
Automobile-Service-Center/
├── src/
│   └── autoservice/
│       ├── Main.java
│       ├── WelcomeFrame.java
│       ├── CustomerPortalFrame.java
│       ├── ServiceCenterFrame.java
│       ├── AppTheme.java
│       ├── ServiceCenterTextStore.java
│       ├── ServiceCenterRepository.java
│       ├── PasswordHasher.java
│       ├── InputHelper.java
│       ├── ServiceCenter.java
│       ├── Customer.java
│       ├── Vehicle.java
│       ├── Mechanic.java
│       ├── ServicePackage.java
│       ├── SparePart.java
│       ├── ServiceRecord.java
│       ├── Invoice.java
│       ├── LoyaltyManager.java
│       └── SalaryManager.java
├── .gitignore
└── README.md
```

## Notes

- This is a desktop demo project, not a production workshop system.
- Data is stored as UTF-8 CSV text in a local `.csv` file.
- Owner credentials are stored locally in the ignored `owner-credentials.properties` file.
- The service packages are sample packages defined in `ServiceCenter.addSamplePackages()`.
- The payroll calculation is a simple demo formula: basic monthly salary + (completed jobs × bonus per job).
