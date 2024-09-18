
export default function NoDataFound() {
    return (
      <div className="flex min-h-[100dvh] items-center justify-center bg-background">
        <div className="max-w-md px-4 py-12 sm:px-6 lg:px-8">
          <div className="flex flex-col items-center justify-center gap-6">
            <FileSearchIcon className="h-16 w-16 text-muted-foreground" />
            <div className="space-y-2 text-center">
              <h2 className="text-2xl font-semibold">No Properties found</h2>
              <p className="text-muted-foreground">It looks like there is no data to display at the moment.</p>
            </div>
          </div>
        </div>
      </div>
    )
  }
  
  function FileSearchIcon(props) {
    return (
      <svg
        {...props}
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      >
        <path d="M14 2v4a2 2 0 0 0 2 2h4" />
        <path d="M4.268 21a2 2 0 0 0 1.727 1H18a2 2 0 0 0 2-2V7l-5-5H6a2 2 0 0 0-2 2v3" />
        <path d="m9 18-1.5-1.5" />
        <circle cx="5" cy="14" r="3" />
      </svg>
    )
  }
  
  
//   function XIcon(props) {
//     return (
//       <svg
//         {...props}
//         xmlns="http://www.w3.org/2000/svg"
//         width="24"
//         height="24"
//         viewBox="0 0 24 24"
//         fill="none"
//         stroke="currentColor"
//         strokeWidth="2"
//         strokeLinecap="round"
//         strokeLinejoin="round"
//       >
//         <path d="M18 6 6 18" />
//         <path d="m6 6 12 12" />
//       </svg>
//     )
//   }