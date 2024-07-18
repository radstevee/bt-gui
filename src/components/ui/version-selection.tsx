import { useState } from "react";
import { Popover, PopoverContent, PopoverTrigger } from "./popover";
import { Button } from "./button";
import { Check, ChevronsUpDown } from "lucide-react";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList
} from "./command";
import { cn } from "@/lib/utils";
import { invoke } from "@tauri-apps/api/tauri";

interface VersionSelectionProps {
  versions: string[]
}

const VersionSelection = ({
  versions
}: VersionSelectionProps) => {
  const [open, setOpen] = useState(false)
  const [value, setValue] = useState("")

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="w-[200px] justify-between"
        >
          {value
            ? versions.find((version) => version === value)
            : "Select a version..."}
          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent
        className="w-[200px] p-0"
        title="Select a version"
      >
        <Command>
          <CommandInput placeholder="Search for a version..." />
          <CommandEmpty>Version not found</CommandEmpty>
          <CommandList>
            <CommandGroup>
              {versions.map((version) => (
                <CommandItem
                  key={version}
                  value={version}
                  onSelect={(currentValue) => {
                    setValue(currentValue === value ? "" : currentValue);
                    setOpen(false);
                    invoke("set_rev", { rev: currentValue })
                  }}
                >
                  <Check
                    className={cn(
                      "mr-2 h-4 w-4",
                      value === version ? "opacity-100" : "opacity-0",
                    )}
                  />
                  {version}
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
}

export default VersionSelection;
