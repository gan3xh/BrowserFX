# BrowserFX: JavaFX-based Web Browser

BrowserFX is a feature-rich, high-performance web browser application built using the JavaFX framework. Designed with both functionality and extensibility in mind, BrowserFX offers a modern browsing experience with a focus on user productivity and customization.

## Documentation

For more detailed information about the BrowserFX project, including the design, implementation, and evaluation, please refer to the [Ganesh_Kumar_projectreport_github.pdf](https://github.com/user-attachments/files/17721540/Ganesh_Kumar_projectreport_github.pdf)

## Motivation and Purpose

1. **User Privacy Focus**: The development of BrowserFX was driven by a deep concern for user anonymity in an era of pervasive data gathering, aiming to enhance privacy and security in web browsing.

2. **Market Need**: Observations of significant user tracking in popular browsers revealed a strong demand for a privacy-respecting browser that meets essential browsing requirements, particularly for sensitive activities like net banking.

3. **User-Centric Design**: BrowserFX empowers users to take back control over their online identities and safeguard their sensitive information from unauthorized access.

4. **Resource Efficiency**: Designed as a lightweight and efficient alternative to traditional browsers, BrowserFX reduces resource consumption, making it ideal for users with limited device processing power.

5. **Simplicity & Accessibility**: The browser provides a straightforward, secure, and convenient browsing experience without compromising on privacy or usability, especially in sensitive areas.


## Features

BrowserFX provides the following core features:

1. **Web Page Rendering**: Leveraging JavaFX's powerful `WebView` and `WebEngine` classes, BrowserFX delivers accurate and responsive rendering of HTML, CSS, and JavaScript content.
2. **Navigation Controls**: Users can navigate the web using the traditional back, forward, and refresh buttons, as well as a fully functional address bar for direct URL input.
3. **Tab Management**: The application supports multiple open tabs, allowing users to easily switch between web pages and maintain their browsing context.
4. **Download Manager**: BrowserFX includes a robust download manager that enables users to initiate, monitor, pause, resume, and cancel file downloads. Real-time progress tracking and download speed metrics are provided.
5. **Customizable UI**: The user interface of BrowserFX is designed to be easily customizable, allowing users to tailor the appearance and layout to their preferences.

## Installation

To run BrowserFX, you'll need to have Java 8 or a later version installed on your system. Follow these steps to get started:

1. Download the BrowserFX.jar file from the project repository.
2. Open a terminal or command prompt and navigate to the directory where you downloaded the JAR file.
3. Run the application using the following command:
   ```
   java -jar BrowserFX.jar
   ```

Alternatively, you can double-click the BrowserFX.jar file to launch the application directly.

## Usage

### Main Interface

The main interface of BrowserFX consists of the following key elements:

![Screenshot 2024-10-16 221210](https://github.com/user-attachments/assets/2afc1101-b978-4327-a2ec-e872a9b77c91)


![Screenshot 2024-10-16 220932](https://github.com/user-attachments/assets/ed74bb57-6847-48f0-96f1-de19e7ab9c40)


1. **Tab Bar**: Allows users to switch between multiple open tabs, managed by the `WebBrowserApp` class.
2. **Navigation Controls**: The `WebPageView` class handles the back, forward, and refresh buttons, as well as the address bar for entering URLs.
3. **Web Content**: The `WebPageView` class, along with the `WebView` and `WebEngine` components from JavaFX, render web pages in the main content area.
4. **Download Manager**: The `FileRetrievalManager` class provides access to the download management functionality.

### Download Manager

The download manager interface, implemented in the `FileRetrievalManager` class, offers comprehensive control over file downloads:

![Screenshot 2024-10-16 220319](https://github.com/user-attachments/assets/624ac29c-de10-4a19-8510-d0de8209bd9f)


![Screenshot 2024-10-16 214745](https://github.com/user-attachments/assets/2244afdd-0379-4b23-9b23-2eee4a49c474)


1. **Download List**: Displays active and completed downloads, managed by the `FileRetrievalTableModel` class.
2. **Progress Bars**: The `ProgressRenderer` class provides the custom table cell renderer for visualizing download progress.
3. **Control Buttons**: Allow users to pause, resume, cancel, and clear downloads, handled by the `FileRetrievalManager`.

## Architecture

BrowserFX follows a modular and extensible architecture, making it easier to maintain, test, and expand the application in the future.

### Key Components

The main components of the BrowserFX application are:

- `BrowserLauncher`: The entry point of the application, responsible for launching the `WebBrowserApp`.
- `WebBrowserApp`: The main JavaFX application class, handling the creation of the user interface and managing the overall application state.
- `WebPageView`: Encapsulates the `WebView` and `WebEngine` for rendering web pages and handling user interactions.
- `FileRetrievalManager`: Manages the download functionality, including the `FileRetriever` class for handling individual file downloads.
- `FileRetrievalTableModel`: Provides the data model for the download manager's table view.
- `ProgressRenderer`: Implements a custom table cell renderer for displaying download progress.

### Modular Design

The modular design of BrowserFX allows for easier maintenance, testing, and potential future expansions. Each component is responsible for a specific set of functionalities, promoting code reusability and maintainability. For example, the `WebPageView` class can be easily modified or replaced without affecting the rest of the application, while the `FileRetrievalManager` can be extended to support additional download features.

## Performance

BrowserFX has been optimized for performance, delivering a smooth and responsive browsing experience.

### Page Load Times

Extensive testing has shown that BrowserFX's page load times are comparable to leading web browsers, with average load times ranging from 1.2 to 3.1 seconds for popular websites.

### Memory Management

The application's memory usage is carefully managed, with gradual increases in memory consumption corresponding to the number of open tabs and the complexity of the loaded web pages. This ensures stable performance even during prolonged browsing sessions.

### Download Speeds

The custom `FileRetriever` class, used by the `FileRetrievalManager`, provides consistent download speeds across various file sizes, with minimal impact on the system's resources.

## Extensibility

BrowserFX's modular design and well-documented codebase make it easy to extend the application's functionality. Developers can integrate custom components, such as unique UI elements, additional browsing features, or specialized data visualizations, by leveraging the existing architecture.

