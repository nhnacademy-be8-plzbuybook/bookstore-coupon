package com.nhnacademy.bookstorecoupon.checker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class DatabaseConnectionCheckerTest {

    private DatabaseConnectionChecker databaseConnectionChecker;

    private DataSource mockDataSource;
    private Connection mockConnection;
    private DatabaseMetaData mockMetaData;

    @BeforeEach
    void setUp() throws SQLException {
        mockDataSource = Mockito.mock(DataSource.class);
        mockConnection = Mockito.mock(Connection.class);
        mockMetaData = Mockito.mock(DatabaseMetaData.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.getMetaData()).thenReturn(mockMetaData);

        databaseConnectionChecker = new DatabaseConnectionChecker(mockDataSource);
    }

    @DisplayName("데이터베이스 연결 성공")
    @Test
    void run() throws Exception {
        when(mockMetaData.getURL()).thenReturn("jdbc:mysql://localhost:3306/testdb");

        databaseConnectionChecker.run();

        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnection, times(1)).getMetaData();
    }

    @DisplayName("데이터베이스 연결 실패")
    @Test
    void run_FailedConnection() throws Exception {
        when(mockDataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        databaseConnectionChecker.run();

        verify(mockDataSource, times(1)).getConnection();
    }
}