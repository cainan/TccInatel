﻿=================================
== TextAnalysisTool.NET ReadMe ==
=================================


------------------
-- Requirements --
------------------
Microsoft .NET Framework 1.1

Note: Because TextAnalysisTool.NET is compiled for .NET 1.1 (instead of .NET
2.0), it is able to run on almost any machine.


-------------------------------
-- Installation Instructions --
-------------------------------
1) Copy TextAnalysisTool.NET.exe (and optionally TextAnalysisTool.NET.txt and
   ReadMe.txt) to a local drive
2) Run TextAnalysisTool.NET.exe


---------------
-- File List --
---------------
TextAnalysisTool.NET.exe - Application
TextAnalysisTool.NET.txt - Documentation file
ReadMe.txt - Requirements, Installation Instructions, File List, Known Issues,
  Notes, History


------------------
-- Known Issues --
------------------
* GDI+ occasionally throws a System.Runtime.InteropServices.ExternalException
  with the message "A generic error occurred in GDI+.". This exceptional
  condition causes TextAnalysisTool.NET to exit after notifying the user.
* The .NET Framework's improper handling of vertical and horizontal scrollbar
  messages for ListBoxes can cause rendering problems for files with very many
  or very long lines.
* Checkboxes don't always repaint properly due to a .NET Framework bug.
* There is extra flicker when resizing the window. This is due to a workaround
  for an even worse .NET Framework repainting bug.
* ListBox limitations on 16-bit OSes (Windows 95/98/Me) prevent files with
  more than 32,767 lines from displaying properly.


-----------
-- Notes --
-----------
One of TextAnalysisTool.NET's core principals is that data should be read one
time as soon as it is available and kept in memory after that. This principle
allows the source data to be amended, changed, or even become unavailable
without affecting the user. Though this principle is an optional convenience
when the data source is a file, it is a strict requirement for data sources
that offer only one chance to read their data (such as the clipboard and plug-
ins). Accordingly, TextAnalysisTool.NET keeps the entire data set in memory at
all times which means that particularly large data sets can tax systems with
comparatively few available resources. The prototypical example is loading a
file that is larger than the amount of memory in the system: performance
suffers. TextAnalysisTool.NET strives to be as responsive as possible under
all circumstances, but is ultimately bound by the resources available to it.


-------------
-- History --
-------------

2006-12-04
----------
* Copy to clipboard includes HTML formatting that preserves the font/ coloring
  of the copied text when pasting (user request)
* Enhanced New Filter and Find dialogs to pre-populate with the text of the
  currently selected line (user request)
* Added saving/loading of Show Only Filtered Lines setting in .TAT filter
  files (user request)
* Added "[x / y]" to enabled filter descriptions to show how many lines are
  currently matching each filter
* Added support for legacy keyboard shortcuts Ctrl+Insert (copy) and Shift+
  Insert (paste) (user request)
* Added black and white to list of filter color options (user request)
* Disabled glyph-mapping to work around a GDI+ bug that causes abrupt
  application termination
* Fixed bug where Help | Documentation didn't clear the Show Only Filtered
  Lines setting

2006-01-11
----------
* Significantly improved performance with large files (load time reduced by
  ~40%, memory use reduced by ~25%, filter time reduced by ~10%)
* Fixed potential null dereference in LineCollectionDisplay.OnDrawItem that is
  extremely rare on all operating systems before Windows Vista (user report)
* Fixed filter display coloration bug when running under .NET 2.0 by working
  around the problem so that the correct behavior is present under both .NET
  1.1 and .NET 2.0 (user report)
* Fixed bug where Ctrl-Alt-[A-Z] didn't work if filtered lines were hidden and
  no lines were displayed
* Fixed bug where the body of the '_' character couldn't be seen with some
  font sizes (including the default)
* Fixed bug where copying text containing '\0' to the clipboard truncated that
  text prematurely
* Fixed premature line truncation bug due to use of default encoding when
  reading files
* Fixed premature line truncation when drawing lines containing '\u0084'
* Improved performance of toggling markers on high line numbers
* Improved performance of drawing extremely long lines
* Set IMAGE_FILE_LARGE_ADDRESS_AWARE flag in PE header to enable access to a
  larger virtual memory area when it is available (ex: on 64-bit machines or
  on machines with the /3GB boot option set)
* Added formatting to the XML content of *.tat filter files for improved human
  readability and better revision control (user request)
* Added support for applying Shift modifier key to Ctrl+F to begin a reverse
  find (this shortcut overrides the shortcut for toggling filter f)
* Added display of plug-in file version to "Installed plug-ins" dialog
* Add About dialog note about protection under United States patent 6,963,878

2003-09-02
----------
* Fixed bug whereby refreshing 0-line file caused an unhandled exception
* Added short version of file name to window title, makes it easier to switch
  among different windows in the taskbar (user request)
* Last directory used for successful file and filter access (via a dialog) is
  remembered, makes it easier to keep files and filters in different locations
  (user request)
* Addressed minor focus issues related to filter display

2003-07-24
----------
* Switched to building on top of .NET Framework 1.1
* Switched to using Windows XP visual styles for dialog controls
* Improved performance during startup, reduced memory requirements
* Fixed horizontal scroll bar bug for long lines with embedded tabs
* Added keyboard shortcuts for toggling and cycling filters (user request)

2003-04-07
----------
* Added support for plug-ins that extend TextAnalysisTool.NET by allowing it
  to read custom file types! (see the "Plug-ins" section of the documentation
  for more details)

2003-03-24
----------
* Added support for coloring lines according to the filter(s) they match (see
  documentation for details) (user request)
* Right-clicking "Regular expression" checkbox opens MSDN online documentation
  for regular expression syntax (user request)
* Require confirmation when Help | Documentation is chosen to avoid
  inadvertently discarding the current lines and filters (user request)
* File sharing modes tweaked for maximum versatility (specifically, to allow
  opening a file that's actively being written to)
* File | Reload tries to preserve the selected line and current view of the
  file
* File open/save dialog boxes remember the last file type they were used with
* Focus rectangle now drawn in line display
* Other minor user interface fixes/improvements

2003-02-28
----------
Initial release of TextAnalysisTool.NET!
Highlights:
* Status bar with file name and filtered/unfiltered line count! (user request)
* Support for copying multi-line selections! (user request)
* Line display colors can be customized! (user request)
* Multiple lines can be marked at the same time! (via multi-line selections)
* Support for drag-and-drop to import text!
* Support for reading UTF-8, Unicode, and Unicode big endian files!
* User efficiency improvements to the Add Filter dialog box!
* Real-time validation for Add Filter/Find input! (great for reg-ex's)
* Filter pane is usable without a mouse!
* Checkboxes in filter pane show which filters are active!

2003-02-21, 2003-02-11, 2003-02-06
----------------------------------
Preview releases to a handful of volunteers
