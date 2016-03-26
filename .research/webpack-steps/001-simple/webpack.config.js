module.exports = {
    devtool: "source-map",
    entry: './src/main.js',
    output: {
        path: __dirname,
        filename: 'bundle.[name].js',
        sourceMapFilename: "bundle.[name].js.map",
        devtoolLineToLine: true,
        pathinfo: true
    }
}
