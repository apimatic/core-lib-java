package io.apimatic.core.configurations.http.client;

import io.apimatic.coreinterfaces.http.proxy.ProxyConfiguration;

/**
 * Represents a proxy configuration with address, port, and optional authentication credentials.
 * This class is an implementation of the {@link ProxyConfiguration} interface.
 */
public class CoreProxyConfiguration implements ProxyConfiguration {

    /**
     * The proxy server address (e.g., IP or domain name).
     */
    private final String address;

    /**
     * The port on which the proxy server is listening.
     */
    private final int port;

    /**
     * The username for proxy authentication, if required.
     */
    private final String username;

    /**
     * The password for proxy authentication, if required.
     */
    private final String password;

    /**
     * Creates a new instance of {@code CoreProxyConfiguration} with the specified address,
     * port, and optional authentication credentials.
     *
     * @param address  The proxy server address.
     * @param port     The proxy server port.
     * @param username The username for proxy authentication (can be {@code null}).
     * @param password The password for proxy authentication (can be {@code null}).
     */
    private CoreProxyConfiguration(String address, int port, String username, String password) {
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Creates a new builder initialized with the current configuration's values.
     * @return a new {@link Builder} instance
     */
    public Builder newBuilder() {
        return new Builder()
          .address(this.address)
          .port(this.port)
          .username(this.username)
          .password(this.password);
    }

    /**
     * Returns a string representation of this proxy configuration.
     * @return string representation of this object
     */
    @Override
    public String toString() {
        return "CoreProxyConfiguration [" +
          "address=" + address +
          ", port=" + port +
          ", username=" + (username != null ? username : "null") +
          ", password=" + (password != null ? password : "null") +
          "]";
    }

    /**
     * Builder class for constructing {@link CoreProxyConfiguration} instances.
     */
    public static class Builder {

        private String address;
        private int port;
        private String username;
        private String password;

        /**
         * Sets the proxy server address.
         * @param address the address to set
         * @return the builder instance
         */
        public Builder address(String address) {
            this.address = address;
            return this;
        }

        /**
         * Sets the proxy server port.
         * @param port the port to set
         * @return the builder instance
         */
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * Sets the proxy username for authentication.
         * @param username the username to set
         * @return the builder instance
         */
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * Sets the proxy password for authentication.
         * @param password the password to set
         * @return the builder instance
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Builds a new {@link CoreProxyConfiguration} instance using the set fields.
         * @return the built {@link CoreProxyConfiguration}
         */
        public CoreProxyConfiguration build() {
            return new CoreProxyConfiguration(address, port, username, password);
        }
    }
}