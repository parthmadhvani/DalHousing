import Sidebar from "./Sidebar";

export default function RemoveAccount() {
    return (
        <div>
            <Sidebar />
            <div className=" mx-auto max-w-md py-12">
                <div className="space-y-4">
                    <h1 className="text-3xl font-bold">Delete Account</h1>
                    <p className="text-muted-foreground">
                        Deleting your account is permanent and cannot be undone. All your data and content will be permanently removed
                        from our servers.
                    </p>
                    <form className="space-y-4">
                        <div className="space-y-2">
                            <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                                Confirm Password
                            </label>
                            <input
                                id="password"
                                type="password"
                                placeholder="Enter your password"
                                required
                                className="block w-full rounded-md border-gray-300 shadow-sm focus:border-primary focus:ring-primary sm:text-sm"
                            />
                        </div>
                        <button
                            type="submit"
                            className="w-full rounded-md bg-red-600 py-2 px-4 text-sm font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
                        >
                            Delete Account
                        </button>
                    </form>
                </div>
            </div>
        </div>

    )
}