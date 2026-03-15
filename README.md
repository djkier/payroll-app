# MotorPH Payroll System

A Java desktop application developed to help MotorPH manage employee payroll operations, including attendance tracking, leave management, payroll computation, and payroll reporting. The system centralizes payroll-related processes to improve accuracy, reduce manual workload, and allow employees to view their personal information and payslips.

## Run Locally
To run the MotorPH Payroll System locally, clone the repository and run the project using either a Java IDE or the command line.
1. Clone the project

```bash
git clone https://github.com/djkier/payroll-app.git
```
2. Ensure Java is installed on your system (Java 21 recommended). Download Java [here](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
3. Run the project using one of the following methods:

**Using an IDE (Recommended)**

Open the project using Apache NetBeans, IntelliJ IDEA, or Eclipse, then run the main application class.

**Using the Command Line**
```bash
mvn clean install
mvn exec:java
```
The application will start and display the login screen.

## Authentication and Access Control
The system currently contains **34 user accounts**. Users log in using their assigned employee credentials.
```bash
	Employee No : <employee number>  
	Username : emp<employee number>  
	Password : <employee number>
```
Example:
```bash
	Employee No : 10001  
	Username : emp10001  
	Password : 10001
```
After successful authentication, the system determines the user's **role** and grants access to system features based on that role.

---
**Role Assignment**

| Role Assignment | Employee No Range |
|--|--|
| Regular Employee | All employees except those marked as probationary |
| Probationary Employee | 10021 – 10031 |
| Human Resource | 10006 – 10009 |
| Payroll/Finance |10010 – 10014|
| IT | 10005 |
| Admin | 10001 – 10004 |

**Access Control by Role**

**Regular & Probationary Employee**
-   View personal employee information
-   View generated payslips
-   File leave requests
-   View leave history
-   Record attendance (time in / time out)

**Human Resource**
-   All employee access
-  Manage employee information (Create, Read, Update, Delete)
-  Review and approve leave requests

**Payroll/Finance**
-   All employee access
-  Update employee salary
-  View employee payslip records
-   Generate payroll reports

**IT**
-   All employee access
-  Change username
-   Reset passwords
-   Activate or deactivate system accounts
- Delete user account that has no employee record

**Admin**
-  All employees access
- Review leave requests
-   View payroll reports

---
#### Probationary Employee Limits

Probationary employees have certain restrictions in the system.
- Payslips will show **zero allowances**.
- Payroll/Finance staff **cannot update** the salary or allowances of probationary employees.
