package io.apimatic.core.configurations.http.client;

import io.apimatic.coreinterfaces.http.proxy.ProxyConfiguration;

/**
 * Represents a proxy configuration with address, port, and optional authentication credentials.
 * This class is an implementation of the {@link ProxyConfiguration} interface.
 */
public final class CoreProxyConfiguration implements ProxyConfiguration {

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
    private CoreProxyConfiguration(final String address, final int port,
      final String username, final String password) {
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
        return new Builder(this.address, this.port)
          .username(this.username)
          .password(this.password);
    }

    /**
     * Returns a string representation of this proxy configuration.
     * @return string representation of this object
     */
    @Override
    public String toString() {
        return "CoreProxyConfiguration ["
          + "address=" + address
          + ", port=" + port
          + ", username=" + username
          + ", password=" + password
          + "]";
    }

    /**
     * Builder class for constructing {@link CoreProxyConfiguration} instances.
     */
    public static class Builder {

        private final String address;
        private final int port;
        private String username;
        private String password;

        /**
         * Creates a new {@code Builder} instance with the specified proxy server details.
         *
         * @param address the hostname or IP address of the proxy server
         * @param port    the port number of the proxy server
         */
        public Builder(final String address, final int port) {
            this.address = address;
            this.port = port;
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
