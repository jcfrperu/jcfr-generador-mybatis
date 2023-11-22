package com.jcfr.generador.dao;

import com.jcfr.apidata.conexiones.ConexionMySQLPooled;
import com.jcfr.apidata.conexiones.ConexionPostgrePooled;
import com.jcfr.apidata.interfaces.IConexion;

import java.sql.Connection;
import java.sql.SQLException;

public class Cnx {

    private static final IConexion cnxConexionMusica = new ConexionPostgrePooled("localhost", "musica-produccion", "fym-blogger", "fym-blogger", "5432");
    private static final IConexion cnxConexionNombres = new ConexionPostgrePooled("localhost", "nombres", "fym-blogger", "fym-blogger", "5432");
    private static final IConexion cnxConexionBackup = new ConexionPostgrePooled("localhost", "blogger-backup", "fym-blogger", "fym-blogger", "5432");
    private static final IConexion cnxConexionPink = new ConexionPostgrePooled("localhost", "pink", "fym-blogger", "fym-blogger", "5432");
    private static final IConexion cnxConexionBlogger = new ConexionPostgrePooled("localhost", "fym-blogger", "fym-blogger", "fym-blogger", "5432");
    private static final IConexion cnxConexionRestobar = new ConexionPostgrePooled("localhost", "restobar", "restobar", "restobar", "5432");
    private static final IConexion cnxConexionLindley = new ConexionPostgrePooled("localhost", "lindley", "lindley", "lindley", "5432");

    private static final IConexion cnxConexionInventario = new ConexionMySQLPooled("localhost", "inventario", "root", "my,JcFR2007ubu", "3306");
    private static final IConexion cnxConexionPediche = new ConexionMySQLPooled("localhost", "pedichebd", "root", "my,JcFR2007ubu", "3306");

    public static Connection getConexionMusica() throws SQLException {
        return cnxConexionMusica.crearConexionJDBC();
    }

    public static Connection getConexionNombres() throws SQLException {
        return cnxConexionNombres.crearConexionJDBC();
    }

    public static Connection getConexionLindley() throws SQLException {
        return cnxConexionLindley.crearConexionJDBC();
    }

    public static Connection getConexionBackup() throws SQLException {
        return cnxConexionBackup.crearConexionJDBC();
    }

    public static Connection getConexionPink() throws SQLException {
        return cnxConexionPink.crearConexionJDBC();
    }

    public static Connection getConexionBlogger() throws SQLException {
        return cnxConexionBlogger.crearConexionJDBC();
    }

    public static Connection getConexionRestobar() throws SQLException {
        return cnxConexionRestobar.crearConexionJDBC();
    }

    public static Connection getConexionInventario() throws SQLException {
        return cnxConexionInventario.crearConexionJDBC();
    }

    public static Connection getConexionPediche() throws SQLException {
        return cnxConexionPediche.crearConexionJDBC();
    }
}
